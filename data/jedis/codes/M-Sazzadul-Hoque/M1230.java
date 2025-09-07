    public static void assertFile(String keyword, File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("%s file %s does not exist", keyword, file));
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(String.format("%s file %s is not a file", keyword, file));
        }
    }
