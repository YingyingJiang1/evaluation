    public void setHeaderFiles(String... headerFiles) {
        final String[] files;
        if (headerFiles == null) {
            files = CommonUtil.EMPTY_STRING_ARRAY;
        }
        else {
            files = headerFiles.clone();
        }

        headerFilesMetadata.clear();

        for (final String headerFile : files) {
            headerFilesMetadata.add(HeaderFileMetadata.createFromFile(headerFile));
        }
    }
