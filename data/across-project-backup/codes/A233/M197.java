    @Scheduled(
            fixedDelayString =
                    "#{applicationProperties.system.tempFileManagement.cleanupIntervalMinutes}",
            timeUnit = TimeUnit.MINUTES)
    public void scheduledCleanup() {
        log.info("Running scheduled temporary file cleanup");
        long maxAgeMillis = tempFileManager.getMaxAgeMillis();

        // Clean up registered temp files (managed by TempFileRegistry)
        int registeredDeletedCount = tempFileManager.cleanupOldTempFiles(maxAgeMillis);
        log.info("Cleaned up {} registered temporary files", registeredDeletedCount);

        // Clean up registered temp directories
        int directoriesDeletedCount = 0;
        for (Path directory : registry.getTempDirectories()) {
            try {
                if (Files.exists(directory)) {
                    GeneralUtils.deleteDirectory(directory);
                    directoriesDeletedCount++;
                    log.debug("Cleaned up temporary directory: {}", directory);
                }
            } catch (IOException e) {
                log.warn("Failed to clean up temporary directory: {}", directory, e);
            }
        }

        // Clean up PDFBox cache file
        cleanupPDFBoxCache();

        // Clean up unregistered temp files based on our cleanup strategy
        boolean containerMode = isContainerMode();
        int unregisteredDeletedCount = cleanupUnregisteredFiles(containerMode, true, maxAgeMillis);

        if (registeredDeletedCount > 0
                || unregisteredDeletedCount > 0
                || directoriesDeletedCount > 0) {
            log.info(
                    "Scheduled cleanup complete. Deleted {} registered files, {} unregistered files, {} directories",
                    registeredDeletedCount,
                    unregisteredDeletedCount,
                    directoriesDeletedCount);
        }
    }
