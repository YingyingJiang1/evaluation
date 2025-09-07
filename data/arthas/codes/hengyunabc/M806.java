    private static Set<ClassLoader> getAllClassLoaders(Instrumentation inst, Filter... filters) {
        Set<ClassLoader> classLoaderSet = new HashSet<ClassLoader>();

        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader != null) {
                if (shouldInclude(classLoader, filters)) {
                    classLoaderSet.add(classLoader);
                }
            }
        }
        return classLoaderSet;
    }
