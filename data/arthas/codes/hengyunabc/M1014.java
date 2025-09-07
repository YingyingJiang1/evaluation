    private boolean shouldOverridesName(Class<? extends AnnotatedCommand> clazz) {
        try {
            clazz.getDeclaredMethod("name");
            return true;
        } catch (NoSuchMethodException ignore) {
            return false;
        }
    }
