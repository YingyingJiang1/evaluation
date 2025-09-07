    private String getModuleSimpleName() {
        final String fullFileName = getFilePath();
        final String[] pathTokens = FILE_SEPARATOR_PATTERN.split(fullFileName);
        final String fileName = pathTokens[pathTokens.length - 1];
        return fileName.substring(0, fileName.length() - JAVA_FILE_EXTENSION.length());
    }
