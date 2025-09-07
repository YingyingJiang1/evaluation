    private List<FontResource> getFontNamesFromLocation(String locationPattern) {
        try {
            Resource[] resources =
                    GeneralUtils.getResourcesFromLocationPattern(locationPattern, resourceLoader);
            return Arrays.stream(resources)
                    .map(
                            resource -> {
                                try {
                                    String filename = resource.getFilename();
                                    if (filename != null) {
                                        int lastDotIndex = filename.lastIndexOf('.');
                                        if (lastDotIndex != -1) {
                                            String name = filename.substring(0, lastDotIndex);
                                            String extension = filename.substring(lastDotIndex + 1);
                                            return new FontResource(name, extension);
                                        }
                                    }
                                    return null;
                                } catch (Exception e) {
                                    throw ExceptionUtils.createRuntimeException(
                                            "error.fontLoadingFailed",
                                            "Error processing font file",
                                            e);
                                }
                            })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw ExceptionUtils.createRuntimeException(
                    "error.fontDirectoryReadFailed", "Failed to read font directory", e);
        }
    }
