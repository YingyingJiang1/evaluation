    public static Field getField(final Class<?> cls, final String fieldName) {
        final Field field = getField(cls, fieldName, false);
        setAccessibleWorkaround(field);
        return field;
    }
