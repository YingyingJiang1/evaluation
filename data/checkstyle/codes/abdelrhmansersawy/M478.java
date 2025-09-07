    public String getConfiguredHeaderPaths() {
        return headerFilesMetadata.stream()
                .map(HeaderFileMetadata::getHeaderFilePath)
                .collect(Collectors.joining(", "));
    }
