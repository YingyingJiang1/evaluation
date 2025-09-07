    public static Set<String> getPropertiesForDocumentation(Class<?> clss, Object instance) {
        final Set<String> properties =
                getProperties(clss).stream()
                    .filter(prop -> {
                        return !isGlobalProperty(clss, prop) && !isUndocumentedProperty(clss, prop);
                    })
                    .collect(Collectors.toCollection(HashSet::new));
        properties.addAll(getNonExplicitProperties(instance, clss));
        return new TreeSet<>(properties);
    }
