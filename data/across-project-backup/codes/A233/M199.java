    private int cleanupUnregisteredFiles(
            boolean containerMode, boolean isScheduled, long maxAgeMillis) {
        AtomicInteger totalDeletedCount = new AtomicInteger(0);

        try {
            ApplicationProperties.TempFileManagement tempFiles =
                    applicationProperties.getSystem().getTempFileManagement();
            Path[] dirsToScan;
            if (tempFiles.isCleanupSystemTemp()
                    && tempFiles.getSystemTempDir() != null
                    && !tempFiles.getSystemTempDir().isEmpty()) {
                Path systemTempPath = getSystemTempPath();
                dirsToScan =
                        new Path[] {
                            systemTempPath,
                            Path.of(tempFiles.getBaseTmpDir()),
                            Path.of(tempFiles.getLibreofficeDir())
                        };
            } else {
                dirsToScan =
                        new Path[] {
                            Path.of(tempFiles.getBaseTmpDir()),
                            Path.of(tempFiles.getLibreofficeDir())
                        };
            }

            // Process each directory
            Arrays.stream(dirsToScan)
                    .filter(Files::exists)
                    .forEach(
                            tempDir -> {
                                try {
                                    String phase = isScheduled ? "scheduled" : "startup";
                                    log.debug(
                                            "Scanning directory for {} cleanup: {}",
                                            phase,
                                            tempDir);

                                    AtomicInteger dirDeletedCount = new AtomicInteger(0);
                                    cleanupDirectoryStreaming(
                                            tempDir,
                                            containerMode,
                                            0,
                                            maxAgeMillis,
                                            isScheduled,
                                            path -> {
                                                dirDeletedCount.incrementAndGet();
                                                if (log.isDebugEnabled()) {
                                                    log.debug(
                                                            "Deleted temp file during {} cleanup: {}",
                                                            phase,
                                                            path);
                                                }
                                            });

                                    int count = dirDeletedCount.get();
                                    totalDeletedCount.addAndGet(count);
                                    if (count > 0) {
                                        log.info(
                                                "Cleaned up {} files/directories in {}",
                                                count,
                                                tempDir);
                                    }
                                } catch (IOException e) {
                                    log.error("Error during cleanup of directory: {}", tempDir, e);
                                }
                            });
        } catch (Exception e) {
            log.error("Error during cleanup of unregistered files", e);
        }

        return totalDeletedCount.get();
    }
