    public static String getType(Field field, String propertyName,
                                 String moduleName, Object instance)
            throws MacroExecutionException {
        final Class<?> fieldClass = getFieldClass(field, propertyName, moduleName, instance);
        return Optional.ofNullable(field)
                .map(nonNullField -> nonNullField.getAnnotation(XdocsPropertyType.class))
                .map(propertyType -> propertyType.value().getDescription())
                .orElseGet(fieldClass::getSimpleName);
    }
