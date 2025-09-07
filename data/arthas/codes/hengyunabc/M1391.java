    public static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        isTrue(field != null, "The field must not be null");
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field);
        }
        return field.get(target);
    }
