    private List<ResultFile> extractZipToIndividualFiles(
            String zipFileId, String originalZipFileName) throws IOException {
        List<ResultFile> extractedFiles = new ArrayList<>();

        MultipartFile zipFile = fileStorage.retrieveFile(zipFileId);

        try (ZipInputStream zipIn =
                ZipSecurity.createHardenedInputStream(
                        new ByteArrayInputStream(zipFile.getBytes()))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    // Use buffered reading for memory safety
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = zipIn.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    byte[] fileContent = out.toByteArray();

                    String contentType = determineContentType(entry.getName());
                    String individualFileId = fileStorage.storeBytes(fileContent, entry.getName());

                    ResultFile resultFile =
                            ResultFile.builder()
                                    .fileId(individualFileId)
                                    .fileName(entry.getName())
                                    .contentType(contentType)
                                    .fileSize(fileContent.length)
                                    .build();

                    extractedFiles.add(resultFile);
                    log.debug(
                            "Extracted file: {} (size: {} bytes)",
                            entry.getName(),
                            fileContent.length);
                }
                zipIn.closeEntry();
            }
        }

        // Clean up the original ZIP file after extraction
        try {
            fileStorage.deleteFile(zipFileId);
            log.debug("Cleaned up original ZIP file: {}", zipFileId);
        } catch (Exception e) {
            log.warn("Failed to clean up original ZIP file {}: {}", zipFileId, e.getMessage());
        }

        return extractedFiles;
    }
