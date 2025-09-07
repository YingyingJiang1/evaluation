    private void cleanupTempFiles() {
        try {
            // Clean up all registered files
            Set<Path> files = registry.getAllRegisteredFiles();
            int deletedCount = 0;

            for (Path file : files) {
                try {
                    if (Files.exists(file)) {
                        Files.deleteIfExists(file);
                        deletedCount++;
                    }
                } catch (IOException e) {
                    log.warn("Failed to delete temp file during shutdown: {}", file, e);
                }
            }

            // Clean up all registered directories
            Set<Path> directories = registry.getTempDirectories();
            for (Path dir : directories) {
                try {
                    if (Files.exists(dir)) {
                        GeneralUtils.deleteDirectory(dir);
                        deletedCount++;
                    }
                } catch (IOException e) {
                    log.warn("Failed to delete temp directory during shutdown: {}", dir, e);
                }
            }

            log.info(
                    "Shutdown cleanup complete. Deleted {} temporary files/directories",
                    deletedCount);

            // Clear the registry
            registry.clear();
        } catch (Exception e) {
            log.error("Error during shutdown cleanup", e);
        }
    }
