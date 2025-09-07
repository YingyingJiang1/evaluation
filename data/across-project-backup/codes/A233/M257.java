    private Object[] processArgsInPlace(Object[] originalArgs, boolean async) {
        if (originalArgs == null || originalArgs.length == 0) {
            return originalArgs;
        }

        // Process all arguments in-place
        for (int i = 0; i < originalArgs.length; i++) {
            Object arg = originalArgs[i];

            if (arg instanceof PDFFile pdfFile) {
                // Case 1: fileId is provided but no fileInput
                if (pdfFile.getFileInput() == null && pdfFile.getFileId() != null) {
                    try {
                        log.debug("Using fileId {} to get file content", pdfFile.getFileId());
                        MultipartFile file = fileStorage.retrieveFile(pdfFile.getFileId());
                        pdfFile.setFileInput(file);
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Failed to resolve file by ID: " + pdfFile.getFileId(), e);
                    }
                }
                // Case 2: For async requests, we need to make a copy of the MultipartFile
                else if (async && pdfFile.getFileInput() != null) {
                    try {
                        log.debug("Making persistent copy of uploaded file for async processing");
                        MultipartFile originalFile = pdfFile.getFileInput();
                        String fileId = fileStorage.storeFile(originalFile);

                        // Store the fileId for later reference
                        pdfFile.setFileId(fileId);

                        // Replace the original MultipartFile with our persistent copy
                        MultipartFile persistentFile = fileStorage.retrieveFile(fileId);
                        pdfFile.setFileInput(persistentFile);

                        log.debug("Created persistent file copy with fileId: {}", fileId);
                    } catch (IOException e) {
                        throw new RuntimeException(
                                "Failed to create persistent copy of uploaded file", e);
                    }
                }
            }
        }

        return originalArgs;
    }
