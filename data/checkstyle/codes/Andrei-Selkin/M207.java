    public static String getFileExtension(String fileNameWithExtension) {
        final String fileName = Paths.get(fileNameWithExtension).toString();
        final int dotIndex = fileName.lastIndexOf('.');
        final String extension;
        if (dotIndex == -1) {
            extension = "";
        }
        else {
            extension = fileName.substring(dotIndex + 1);
        }
        return extension;
    }
