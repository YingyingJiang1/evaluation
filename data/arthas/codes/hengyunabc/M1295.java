    private static ClassLoader wrap(ClassLoader classLoader) {
        if (classLoader != null) {
            return classLoader;
        }
        return FAKEBOOTSTRAPCLASSLOADER;
    }
