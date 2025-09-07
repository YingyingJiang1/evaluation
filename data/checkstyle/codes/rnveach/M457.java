    private String getFileName(File file) {
        String fileName = file.getName();

        if (ignoreFileNameExtensions) {
            fileName = CommonUtil.getFileNameWithoutExtension(fileName);
        }

        return fileName;
    }
