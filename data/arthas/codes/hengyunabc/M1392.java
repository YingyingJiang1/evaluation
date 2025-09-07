    public static Object readStaticField(final Field field, final boolean forceAccess) throws IllegalAccessException {
        isTrue(field != null, "The field must not be null");
        isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
        return readField(field, (Object) null, forceAccess);
    }
