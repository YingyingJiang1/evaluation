    private static boolean isGlobalProperty(Class<?> clss, String propertyName) {
        return AbstractCheck.class.isAssignableFrom(clss)
                    && CHECK_PROPERTIES.contains(propertyName)
                || AbstractJavadocCheck.class.isAssignableFrom(clss)
                    && JAVADOC_CHECK_PROPERTIES.contains(propertyName)
                || AbstractFileSetCheck.class.isAssignableFrom(clss)
                    && FILESET_PROPERTIES.contains(propertyName);
    }
