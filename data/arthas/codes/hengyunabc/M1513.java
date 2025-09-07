    public static String loggingDir() {
        if (logFile != null && !logFile.isEmpty()) {
            String parent = new File(logFile).getParent();
            if (parent != null) {
                return parent;
            }
        }
        return new File("").getAbsolutePath();
    }
