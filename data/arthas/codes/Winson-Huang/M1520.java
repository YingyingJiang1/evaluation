    private static File extractEmbeddedLib() {
        String resourceName = "/" + getPlatformTag() + "/libasyncProfiler.so";
        InputStream in = AsyncProfiler.class.getResourceAsStream(resourceName);
        if (in == null) {
            return null;
        }

        try {
            String extractPath = System.getProperty("one.profiler.extractPath");
            File file = File.createTempFile("libasyncProfiler-", ".so",
                    extractPath == null || extractPath.isEmpty() ? null : new File(extractPath));
            try (FileOutputStream out = new FileOutputStream(file)) {
                byte[] buf = new byte[32000];
                for (int bytes; (bytes = in.read(buf)) >= 0; ) {
                    out.write(buf, 0, bytes);
                }
            }
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
