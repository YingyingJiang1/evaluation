    public static void writeStaticField(final Field field, final Object value, final boolean forceAccess) throws IllegalAccessException {
        isTrue(field != null, "The field must not be null");
        isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", field.getDeclaringClass().getName(),
                field.getName());
        writeField(field, (Object) null, value, forceAccess);
    }
