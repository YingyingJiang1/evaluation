    public static Properties getResolvedProperties(Properties properties)
            throws CheckstyleException {
        final Set<String> unresolvedPropertyNames =
            new HashSet<>(properties.stringPropertyNames());
        Iterator<String> unresolvedPropertyIterator = unresolvedPropertyNames.iterator();
        final Map<Object, Object> comparisonProperties = new Properties();

        while (unresolvedPropertyIterator.hasNext()) {
            final String propertyName = unresolvedPropertyIterator.next();
            String propertyValue = properties.getProperty(propertyName);
            final Matcher matcher = PROPERTY_VARIABLE_PATTERN.matcher(propertyValue);

            while (matcher.find()) {
                final String propertyVariableExpression = matcher.group();
                final String unresolvedPropertyName =
                    getPropertyNameFromExpression(propertyVariableExpression);

                final String resolvedPropertyValue =
                    properties.getProperty(unresolvedPropertyName);

                if (resolvedPropertyValue != null) {
                    propertyValue = propertyValue.replace(propertyVariableExpression,
                        resolvedPropertyValue);
                    properties.setProperty(propertyName, propertyValue);
                }
            }

            if (allChainedPropertiesAreResolved(propertyValue)) {
                unresolvedPropertyIterator.remove();
            }

            if (!unresolvedPropertyIterator.hasNext()) {

                if (comparisonProperties.equals(properties)) {
                    // At this point, we will have not resolved any properties in two iterations,
                    // so unresolvable properties exist.
                    throw new CheckstyleException(UNDEFINED_PROPERTY_MESSAGE
                        + unresolvedPropertyNames);
                }
                comparisonProperties.putAll(properties);
                unresolvedPropertyIterator = unresolvedPropertyNames.iterator();
            }

        }
        return properties;
    }
