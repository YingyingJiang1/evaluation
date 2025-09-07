    @GetMapping("/api/v1/general/files/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") String fileId) {
        try {
            // Verify file exists
            if (!fileStorage.fileExists(fileId)) {
                return ResponseEntity.notFound().build();
            }

            // Retrieve file content
            byte[] fileContent = fileStorage.retrieveBytes(fileId);

            // Find the file metadata from any job that contains this file
            // This is for getting the original filename and content type
            ResultFile resultFile = taskManager.findResultFileByFileId(fileId);

            String fileName = resultFile != null ? resultFile.getFileName() : "download";
            String contentType =
                    resultFile != null ? resultFile.getContentType() : "application/octet-stream";

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", createContentDispositionHeader(fileName))
                    .body(fileContent);
        } catch (Exception e) {
            log.error("Error retrieving file {}: {}", fileId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error retrieving file: " + e.getMessage());
        }
    }
