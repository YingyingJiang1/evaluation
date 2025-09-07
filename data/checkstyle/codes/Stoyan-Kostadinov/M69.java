    public static String getPropertyDescription(
            String propertyName, DetailNode javadoc, String moduleName)
            throws MacroExecutionException {
        final String description;
        if (TOKENS.equals(propertyName)) {
            description = "tokens to check";
        }
        else if (JAVADOC_TOKENS.equals(propertyName)) {
            description = "javadoc tokens to check";
        }
        else {
            final String descriptionString = SETTER_PATTERN.matcher(
                    DescriptionExtractor.getDescriptionFromJavadoc(javadoc, moduleName))
                    .replaceFirst("");

            final String firstLetterCapitalized = descriptionString.substring(0, 1)
                    .toUpperCase(Locale.ROOT);
            description = firstLetterCapitalized + descriptionString.substring(1);
        }
        return description;
    }
