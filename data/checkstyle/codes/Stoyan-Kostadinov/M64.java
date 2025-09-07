    public static Map<String, DetailNode> getPropertiesJavadocs(Set<String> properties,
                                                                String moduleName, Path modulePath)
            throws MacroExecutionException {
        // lazy initialization
        if (SUPER_CLASS_PROPERTIES_JAVADOCS.isEmpty()) {
            processSuperclasses();
        }

        processModule(moduleName, modulePath);

        final Map<String, DetailNode> unmodifiableJavadocs =
                ClassAndPropertiesSettersJavadocScraper.getJavadocsForModuleOrProperty();
        final Map<String, DetailNode> javadocs = new LinkedHashMap<>(unmodifiableJavadocs);

        properties.forEach(property -> {
            final DetailNode superClassPropertyJavadoc =
                    SUPER_CLASS_PROPERTIES_JAVADOCS.get(property);
            if (superClassPropertyJavadoc != null) {
                javadocs.putIfAbsent(property, superClassPropertyJavadoc);
            }
        });

        assertAllPropertySetterJavadocsAreFound(properties, moduleName, javadocs);

        return javadocs;
    }
