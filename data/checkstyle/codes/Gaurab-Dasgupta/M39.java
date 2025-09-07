    private static String getPackageName(String filePath) {
        final Deque<String> result = new ArrayDeque<>();
        final String[] filePathTokens = FILE_SEPARATOR_PATTERN.split(filePath);
        for (int i = filePathTokens.length - 1; i >= 0; i--) {
            if ("java".equals(filePathTokens[i]) || "resources".equals(filePathTokens[i])) {
                break;
            }
            result.addFirst(filePathTokens[i]);
        }
        final String fileName = result.removeLast();
        result.addLast(fileName.substring(0, fileName.length() - JAVA_FILE_EXTENSION.length()));
        return String.join(".", result);
    }
