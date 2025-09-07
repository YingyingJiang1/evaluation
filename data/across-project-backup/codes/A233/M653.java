    private List<File> prepareFilesForProcessing(File[] files, Path processingDir)
            throws IOException {
        List<File> filesToProcess = new ArrayList<>();
        for (File file : files) {
            Path targetPath = resolveUniqueFilePath(processingDir, file.getName());

            // Retry with exponential backoff
            int maxRetries = 3;
            int retryDelayMs = 500;
            boolean moved = false;

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    moved = true;
                    break;
                } catch (FileSystemException e) {
                    if (attempt < maxRetries) {
                        log.info("File move failed (attempt {}), retrying...", attempt);
                        try {
                            Thread.sleep(retryDelayMs * (int) Math.pow(2, attempt - 1));
                        } catch (InterruptedException e1) {
                            log.error("prepareFilesForProcessing failure", e);
                        }
                    }
                }
            }

            if (moved) {
                filesToProcess.add(targetPath.toFile());
            } else {
                log.error("Failed to move file after {} attempts: {}", maxRetries, file.getName());
            }
        }
        return filesToProcess;
    }
