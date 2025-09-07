    void enhanceClassLoader() throws IOException, UnmodifiableClassException {
        if (configure.getEnhanceLoaders() == null) {
            return;
        }
        Set<String> loaders = new HashSet<String>();
        for (String s : configure.getEnhanceLoaders().split(",")) {
            loaders.add(s.trim());
        }

        // 增强 ClassLoader#loadClsss ，解决一些ClassLoader加载不到 SpyAPI的问题
        // https://github.com/alibaba/arthas/issues/1596
        byte[] classBytes = IOUtils.getBytes(ArthasBootstrap.class.getClassLoader()
                .getResourceAsStream(ClassLoader_Instrument.class.getName().replace('.', '/') + ".class"));

        SimpleClassMatcher matcher = new SimpleClassMatcher(loaders);
        InstrumentConfig instrumentConfig = new InstrumentConfig(AsmUtils.toClassNode(classBytes), matcher);

        InstrumentParseResult instrumentParseResult = new InstrumentParseResult();
        instrumentParseResult.addInstrumentConfig(instrumentConfig);
        classLoaderInstrumentTransformer = new InstrumentTransformer(instrumentParseResult);
        instrumentation.addTransformer(classLoaderInstrumentTransformer, true);

        if (loaders.size() == 1 && loaders.contains(ClassLoader.class.getName())) {
            // 如果只增强 java.lang.ClassLoader，可以减少查找过程
            instrumentation.retransformClasses(ClassLoader.class);
        } else {
            InstrumentationUtils.trigerRetransformClasses(instrumentation, loaders);
        }
    }
