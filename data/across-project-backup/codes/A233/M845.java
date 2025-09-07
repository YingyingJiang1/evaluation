    @GetMapping("/api/v1/general/files/{fileId}/metadata")
    public ResponseEntity<?> getFileMetadata(@PathVariable("fileId") String fileId) {
        try {
            // Verify file exists
            if (!fileStorage.fileExists(fileId)) {
                return ResponseEntity.notFound().build();
            }

            // Find the file metadata from any job that contains this file
            ResultFile resultFile = taskManager.findResultFileByFileId(fileId);

            if (resultFile != null) {
                return ResponseEntity.ok(resultFile);
            } else {
                // File exists but no metadata found, get basic info efficiently
                long fileSize = fileStorage.getFileSize(fileId);
                return ResponseEntity.ok(
                        Map.of(
                                "fileId",
                                fileId,
                                "fileName",
                                "unknown",
                                "contentType",
                                "application/octet-stream",
                                "fileSize",
                                fileSize));
            }
        } catch (Exception e) {
            log.error("Error retrieving file metadata {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error retrieving file metadata: " + e.getMessage());
        }
    }
