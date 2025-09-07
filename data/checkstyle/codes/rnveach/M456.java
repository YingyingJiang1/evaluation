    @Override
    protected void processFiltered(File file, FileText fileText) throws CheckstyleException {
        final String fileName = getFileName(file);
        final String folderPath = getFolderPath(file);

        if (isMatchFolder(folderPath) && isMatchFile(fileName)) {
            log();
        }
    }
