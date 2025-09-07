    @GetMapping("/api/v1/general/job/{jobId}/result")
    public ResponseEntity<?> getJobResult(@PathVariable("jobId") String jobId) {
        JobResult result = taskManager.getJobResult(jobId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        if (!result.isComplete()) {
            return ResponseEntity.badRequest().body("Job is not complete yet");
        }

        if (result.getError() != null) {
            return ResponseEntity.badRequest().body("Job failed: " + result.getError());
        }

        // Handle multiple files - return metadata for client to download individually
        if (result.hasMultipleFiles()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Map.of(
                                    "jobId",
                                    jobId,
                                    "hasMultipleFiles",
                                    true,
                                    "files",
                                    result.getAllResultFiles()));
        }

        // Handle single file (download directly)
        if (result.hasFiles() && !result.hasMultipleFiles()) {
            try {
                List<ResultFile> files = result.getAllResultFiles();
                ResultFile singleFile = files.get(0);
                byte[] fileContent = fileStorage.retrieveBytes(singleFile.getFileId());
                return ResponseEntity.ok()
                        .header("Content-Type", singleFile.getContentType())
                        .header(
                                "Content-Disposition",
                                createContentDispositionHeader(singleFile.getFileName()))
                        .body(fileContent);
            } catch (Exception e) {
                log.error("Error retrieving file for job {}: {}", jobId, e.getMessage(), e);
                return ResponseEntity.internalServerError()
                        .body("Error retrieving file: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(result.getResult());
    }
