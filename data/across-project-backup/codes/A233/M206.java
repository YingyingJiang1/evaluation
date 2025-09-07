    private void processJobResult(String jobId, Object result) {
        try {
            if (result instanceof byte[]) {
                // Store byte array directly to disk to avoid double memory consumption
                String fileId = fileStorage.storeBytes((byte[]) result, "result.pdf");
                taskManager.setFileResult(jobId, fileId, "result.pdf", "application/pdf");
                log.debug("Stored byte[] result with fileId: {}", fileId);

                // Let the byte array get collected naturally in the next GC cycle
                // We don't need to force System.gc() which can be harmful
            } else if (result instanceof ResponseEntity) {
                ResponseEntity<?> response = (ResponseEntity<?>) result;
                Object body = response.getBody();

                if (body instanceof byte[]) {
                    // Extract filename from content-disposition header if available
                    String filename = "result.pdf";
                    String contentType = "application/pdf";

                    if (response.getHeaders().getContentDisposition() != null) {
                        String disposition =
                                response.getHeaders().getContentDisposition().toString();
                        if (disposition.contains("filename=")) {
                            filename =
                                    disposition.substring(
                                            disposition.indexOf("filename=") + 9,
                                            disposition.lastIndexOf("\""));
                        }
                    }

                    if (response.getHeaders().getContentType() != null) {
                        contentType = response.getHeaders().getContentType().toString();
                    }

                    // Store byte array directly to disk
                    String fileId = fileStorage.storeBytes((byte[]) body, filename);
                    taskManager.setFileResult(jobId, fileId, filename, contentType);
                    log.debug("Stored ResponseEntity<byte[]> result with fileId: {}", fileId);

                    // Let the GC handle the memory naturally
                } else {
                    // Check if the response body contains a fileId
                    if (body != null && body.toString().contains("fileId")) {
                        try {
                            // Try to extract fileId using reflection
                            java.lang.reflect.Method getFileId =
                                    body.getClass().getMethod("getFileId");
                            String fileId = (String) getFileId.invoke(body);

                            if (fileId != null && !fileId.isEmpty()) {
                                // Try to get filename and content type
                                String filename = "result.pdf";
                                String contentType = "application/pdf";

                                try {
                                    java.lang.reflect.Method getOriginalFileName =
                                            body.getClass().getMethod("getOriginalFilename");
                                    String origName = (String) getOriginalFileName.invoke(body);
                                    if (origName != null && !origName.isEmpty()) {
                                        filename = origName;
                                    }
                                } catch (Exception e) {
                                    log.debug(
                                            "Could not get original filename: {}", e.getMessage());
                                }

                                try {
                                    java.lang.reflect.Method getContentType =
                                            body.getClass().getMethod("getContentType");
                                    String ct = (String) getContentType.invoke(body);
                                    if (ct != null && !ct.isEmpty()) {
                                        contentType = ct;
                                    }
                                } catch (Exception e) {
                                    log.debug("Could not get content type: {}", e.getMessage());
                                }

                                taskManager.setFileResult(jobId, fileId, filename, contentType);
                                log.debug("Extracted fileId from response body: {}", fileId);

                                taskManager.setComplete(jobId);
                                return;
                            }
                        } catch (Exception e) {
                            log.debug(
                                    "Failed to extract fileId from response body: {}",
                                    e.getMessage());
                        }
                    }

                    // Store generic result
                    taskManager.setResult(jobId, body);
                }
            } else if (result instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) result;
                String fileId = fileStorage.storeFile(file);
                taskManager.setFileResult(
                        jobId, fileId, file.getOriginalFilename(), file.getContentType());
                log.debug("Stored MultipartFile result with fileId: {}", fileId);
            } else {
                // Check if result has a fileId field
                if (result != null) {
                    try {
                        // Try to extract fileId using reflection
                        java.lang.reflect.Method getFileId =
                                result.getClass().getMethod("getFileId");
                        String fileId = (String) getFileId.invoke(result);

                        if (fileId != null && !fileId.isEmpty()) {
                            // Try to get filename and content type
                            String filename = "result.pdf";
                            String contentType = "application/pdf";

                            try {
                                java.lang.reflect.Method getOriginalFileName =
                                        result.getClass().getMethod("getOriginalFilename");
                                String origName = (String) getOriginalFileName.invoke(result);
                                if (origName != null && !origName.isEmpty()) {
                                    filename = origName;
                                }
                            } catch (Exception e) {
                                log.debug("Could not get original filename: {}", e.getMessage());
                            }

                            try {
                                java.lang.reflect.Method getContentType =
                                        result.getClass().getMethod("getContentType");
                                String ct = (String) getContentType.invoke(result);
                                if (ct != null && !ct.isEmpty()) {
                                    contentType = ct;
                                }
                            } catch (Exception e) {
                                log.debug("Could not get content type: {}", e.getMessage());
                            }

                            taskManager.setFileResult(jobId, fileId, filename, contentType);
                            log.debug("Extracted fileId from result object: {}", fileId);

                            taskManager.setComplete(jobId);
                            return;
                        }
                    } catch (Exception e) {
                        log.debug(
                                "Failed to extract fileId from result object: {}", e.getMessage());
                    }
                }

                // Default case: store the result as is
                taskManager.setResult(jobId, result);
            }

            taskManager.setComplete(jobId);
        } catch (Exception e) {
            log.error("Error processing job result: {}", e.getMessage(), e);
            taskManager.setError(jobId, "Error processing result: " + e.getMessage());
        }
    }
