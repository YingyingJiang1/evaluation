    @Override
    public void init() {
        if (fileNamePattern == null && folderPattern == null) {
            fileNamePattern = CommonUtil.createPattern("\\s");
        }
    }
