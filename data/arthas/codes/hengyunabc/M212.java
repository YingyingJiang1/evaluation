    private static ClassLoader loadOrDefineClassLoader(File arthasCoreJarFile) throws Throwable {
        if (arthasClassLoader == null) {
            arthasClassLoader = new ArthasClassloader(new URL[]{arthasCoreJarFile.toURI().toURL()});
        }
        return arthasClassLoader;
    }
