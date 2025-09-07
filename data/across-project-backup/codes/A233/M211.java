    public void setFileResult(
            String jobId, String fileId, String originalFileName, String contentType) {
        JobResult jobResult = getOrCreateJobResult(jobId);

        // Check if this is a ZIP file that should be extracted
        if (isZipFile(contentType, originalFileName)) {
            try {
                List<ResultFile> extractedFiles =
                        extractZipToIndividualFiles(fileId, originalFileName);
                if (!extractedFiles.isEmpty()) {
                    jobResult.completeWithFiles(extractedFiles);
                    log.debug(
                            "Set multiple file results for job ID: {} with {} files extracted from ZIP",
                            jobId,
                            extractedFiles.size());
                    return;
                }
            } catch (Exception e) {
                log.warn(
                        "Failed to extract ZIP file for job {}: {}. Falling back to single file result.",
                        jobId,
                        e.getMessage());
            }
        }

        // Handle as single file using new ResultFile approach
        try {
            long fileSize = fileStorage.getFileSize(fileId);
            jobResult.completeWithSingleFile(fileId, originalFileName, contentType, fileSize);
            log.debug("Set single file result for job ID: {} with file ID: {}", jobId, fileId);
        } catch (Exception e) {
            log.warn(
                    "Failed to get file size for job {}: {}. Using size 0.", jobId, e.getMessage());
            jobResult.completeWithSingleFile(fileId, originalFileName, contentType, 0);
        }
    }
