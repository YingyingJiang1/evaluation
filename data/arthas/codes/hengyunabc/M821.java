    private Map<Class<?>, File> dump(Instrumentation inst, Set<Class<?>> classes) throws UnmodifiableClassException {
        ClassDumpTransformer transformer = null;
        if (directory != null) {
            transformer = new ClassDumpTransformer(classes, new File(directory));
        } else {
            transformer = new ClassDumpTransformer(classes);
        }
        InstrumentationUtils.retransformClasses(inst, transformer, classes);
        return transformer.getDumpResult();
    }
