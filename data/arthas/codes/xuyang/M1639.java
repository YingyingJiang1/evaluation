    private static boolean isBasicType(Class<?> clazz) {
        if (String.class.equals(clazz) || Integer.class.equals(clazz) || Long.class.equals(clazz)
                || Float.class.equals(clazz) || Double.class.equals(clazz) || Boolean.class.equals(clazz)) {
            return true;
        }
        return false;
    }
