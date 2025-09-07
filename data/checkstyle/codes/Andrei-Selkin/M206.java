    public static String getFileNameWithoutExtension(String fullFilename) {
        final String fileName = new File(fullFilename).getName();
        final int dotIndex = fileName.lastIndexOf('.');
        final String fileNameWithoutExtension;
        if (dotIndex == -1) {
            fileNameWithoutExtension = fileName;
        }
        else {
            fileNameWithoutExtension = fileName.substring(0, dotIndex);
        }
        return fileNameWithoutExtension;
    }
