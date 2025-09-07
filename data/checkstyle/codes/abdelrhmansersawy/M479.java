    @Override
    public Set<String> getExternalResourceLocations() {
        return headerFilesMetadata.stream()
                .map(HeaderFileMetadata::getHeaderFileUri)
                .map(URI::toASCIIString)
                .collect(Collectors.toUnmodifiableSet());
    }
