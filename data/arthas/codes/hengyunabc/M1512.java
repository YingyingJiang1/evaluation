    public static String loggingFile() {
        if (logFile == null || logFile.trim().isEmpty()) {
            return "arthas.log";
        }
        return logFile;
    }
