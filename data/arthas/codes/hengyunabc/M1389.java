    public static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        isTrue(target != null, "target object must not be null");
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        isTrue(field != null, "Cannot locate declared field %s.%s", cls, fieldName);
        // already forced access above, don't repeat it here:
        return readField(field, target, false);
    }
