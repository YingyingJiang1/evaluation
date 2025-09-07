    public static <T> T getFieldValueByField(Object target, Field field) throws IllegalArgumentException, IllegalAccessException {
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            //noinspection unchecked
            return (T) field.get(target);
        } finally {
            field.setAccessible(isAccessible);
        }
    }
