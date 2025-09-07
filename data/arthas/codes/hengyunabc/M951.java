    private static byte[] loadClassBytes(Class<?> clazz) {
        try {
            InputStream stream = LoggerCommand.class.getClassLoader()
                    .getResourceAsStream(clazz.getName().replace('.', '/') + ".class");

            return IOUtils.getBytes(stream);
        } catch (IOException e) {
            // ignore
            return null;
        }
    }
