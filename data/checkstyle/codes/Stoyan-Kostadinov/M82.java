    private static Class<?> getFieldClass(Field field, String propertyName,
                                          String moduleName, Object instance)
            throws MacroExecutionException {
        Class<?> result = null;

        if (PROPERTIES_ALLOWED_GET_TYPES_FROM_METHOD
                .contains(moduleName + DOT + propertyName)) {
            result = getPropertyClass(propertyName, instance);
        }
        if (field != null && result == null) {
            result = field.getType();
        }
        if (result == null) {
            throw new MacroExecutionException(
                    "Could not find field " + propertyName + " in class " + moduleName);
        }
        if (field != null && (result == List.class || result == Set.class)) {
            final ParameterizedType type = (ParameterizedType) field.getGenericType();
            final Class<?> parameterClass = (Class<?>) type.getActualTypeArguments()[0];

            if (parameterClass == Integer.class) {
                result = int[].class;
            }
            else if (parameterClass == String.class) {
                result = String[].class;
            }
            else if (parameterClass == Pattern.class) {
                result = Pattern[].class;
            }
            else {
                final String message = "Unknown parameterized type: "
                        + parameterClass.getSimpleName();
                throw new MacroExecutionException(message);
            }
        }
        else if (result == BitSet.class) {
            result = int[].class;
        }

        return result;
    }
