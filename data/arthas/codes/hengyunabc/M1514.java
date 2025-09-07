    public static String cacheDir() {
        File logsDir = new File(loggingDir()).getParentFile();
        if (logsDir.exists()) {
            File arthasCacheDir = new File(logsDir, "arthas-cache");
            arthasCacheDir.mkdirs();
            return arthasCacheDir.getAbsolutePath();
        } else {
            File arthasCacheDir = new File("arthas-cache");
            arthasCacheDir.mkdirs();
            return arthasCacheDir.getAbsolutePath();
        }
    }
