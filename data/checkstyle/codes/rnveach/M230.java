    private static boolean hasDefaultConstructor(Class<?> clazz) {
        boolean result = false;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                result = true;
                break;
            }
        }
        return result;
    }
