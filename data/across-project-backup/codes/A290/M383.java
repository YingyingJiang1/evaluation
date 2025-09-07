    private static Class<?> getClassGeneric(Field field, Class<?> classGeneric) {
        if (classGeneric != null) {
            return classGeneric;
        }
        if (field == null) {
            return defaultClassGeneric;
        }
        Type type = field.getGenericType();
        if (!(type instanceof ParameterizedType)) {
            return defaultClassGeneric;
        }
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) {
            return defaultClassGeneric;
        }
        Type actualType = types[0];
        if (!(actualType instanceof Class<?>)) {
            return defaultClassGeneric;
        }
        return (Class<?>)actualType;
    }
