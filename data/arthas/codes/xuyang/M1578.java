    public static void appendSpyJar(Instrumentation instrumentation) throws IOException {
        // find spy target/classes directory
        String file = DemoBootstrap.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        File spyClassDir = new File(file, "../../../spy/target/classes").getAbsoluteFile();

        File destJarFile = new File(file, "../../../spy/target/test-spy.jar").getAbsoluteFile();

        ZipUtil.pack(spyClassDir, destJarFile);

        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(destJarFile));

    }
