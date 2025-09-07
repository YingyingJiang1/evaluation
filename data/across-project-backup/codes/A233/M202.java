    private boolean shouldDeleteFile(
            Path path, String fileName, boolean containerMode, long maxAgeMillis) {
        // First check if it matches our known temp file patterns
        boolean isOurTempFile = IS_OUR_TEMP_FILE.test(fileName);
        boolean isSystemTempFile = IS_SYSTEM_TEMP_FILE.test(fileName);

        // Normal operation - check against temp file patterns
        boolean shouldDelete = isOurTempFile || (containerMode && isSystemTempFile);

        // Get file info for age checks
        long lastModified = 0;
        long currentTime = System.currentTimeMillis();
        boolean isEmptyFile = false;

        try {
            lastModified = Files.getLastModifiedTime(path).toMillis();
            // Special case for zero-byte files - these are often corrupted temp files
            if (Files.size(path) == 0) {
                isEmptyFile = true;
                // For empty files, use a shorter timeout (5 minutes)
                // Delete empty files older than 5 minutes
                if ((currentTime - lastModified) > 5 * 60 * 1000) {
                    shouldDelete = true;
                }
            }
        } catch (IOException e) {
            log.debug("Could not check file info, skipping: {}", path);
        }

        // Check file age against maxAgeMillis only if it's not an empty file that we've already
        // decided to delete
        if (!isEmptyFile && shouldDelete && maxAgeMillis > 0) {
            // In normal mode, check age against maxAgeMillis
            shouldDelete = (currentTime - lastModified) > maxAgeMillis;
        }

        return shouldDelete;
    }
