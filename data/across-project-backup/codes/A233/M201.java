    private void cleanupDirectoryStreaming(
            Path directory,
            boolean containerMode,
            int depth,
            long maxAgeMillis,
            boolean isScheduled,
            Consumer<Path> onDeleteCallback)
            throws IOException {

        if (depth > MAX_RECURSION_DEPTH) {
            log.debug("Maximum directory recursion depth reached for: {}", directory);
            return;
        }

        java.util.List<Path> subdirectories = new java.util.ArrayList<>();

        try (Stream<Path> pathStream = Files.list(directory)) {
            pathStream.forEach(
                    path -> {
                        try {
                            String fileName = path.getFileName().toString();

                            if (SHOULD_SKIP.test(fileName)) {
                                return;
                            }

                            if (Files.isDirectory(path)) {
                                subdirectories.add(path);
                                return;
                            }

                            if (registry.contains(path.toFile())) {
                                return;
                            }

                            if (shouldDeleteFile(path, fileName, containerMode, maxAgeMillis)) {
                                try {
                                    Files.deleteIfExists(path);
                                    onDeleteCallback.accept(path);
                                } catch (IOException e) {
                                    if (e.getMessage() != null
                                            && e.getMessage()
                                                    .contains("being used by another process")) {
                                        log.debug("File locked, skipping delete: {}", path);
                                    } else {
                                        log.warn("Failed to delete temp file: {}", path, e);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Error processing path: {}", path, e);
                        }
                    });
        }

        for (Path subdirectory : subdirectories) {
            try {
                cleanupDirectoryStreaming(
                        subdirectory,
                        containerMode,
                        depth + 1,
                        maxAgeMillis,
                        isScheduled,
                        onDeleteCallback);
            } catch (IOException e) {
                log.warn("Error processing subdirectory: {}", subdirectory, e);
            }
        }
    }
