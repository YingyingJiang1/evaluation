    private File[] collectFilesForProcessing(Path dir, Path jsonFile, PipelineOperation operation)
            throws IOException {

        List<String> inputExtensions =
                apiDocService.getExtensionTypes(false, operation.getOperation());
        log.info(
                "Allowed extensions for operation {}: {}",
                operation.getOperation(),
                inputExtensions);

        boolean allowAllFiles = inputExtensions.contains("ALL");

        try (Stream<Path> paths = Files.list(dir)) {
            File[] files =
                    paths.filter(
                                    path -> {
                                        if (Files.isDirectory(path)) {
                                            return false;
                                        }
                                        if (path.equals(jsonFile)) {
                                            return false;
                                        }

                                        // Get file extension
                                        String filename = path.getFileName().toString();
                                        String extension =
                                                filename.contains(".")
                                                        ? filename.substring(
                                                                        filename.lastIndexOf(".")
                                                                                + 1)
                                                                .toLowerCase()
                                                        : "";

                                        // Check against allowed extensions
                                        boolean isAllowed =
                                                allowAllFiles
                                                        || inputExtensions.contains(
                                                                extension.toLowerCase());
                                        if (!isAllowed) {
                                            log.info(
                                                    "Skipping file with unsupported extension: {} ({})",
                                                    filename,
                                                    extension);
                                        }
                                        return isAllowed;
                                    })
                            .map(Path::toAbsolutePath)
                            .filter(
                                    path -> {
                                        boolean isReady =
                                                fileMonitor.isFileReadyForProcessing(path);
                                        if (!isReady) {
                                            log.info(
                                                    "File not ready for processing (locked/created last 5s): {}",
                                                    path);
                                        }
                                        return isReady;
                                    })
                            .map(Path::toFile)
                            .toArray(File[]::new);
            log.info(
                    "Collected {} files for processing for {}",
                    files.length,
                    dir.toAbsolutePath().toString());
            return files;
        }
    }
