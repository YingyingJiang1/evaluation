    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String customTempDir = System.getenv("STIRLING_TEMPFILES_DIRECTORY");
        if (customTempDir == null || customTempDir.isEmpty()) {
            customTempDir = System.getProperty("stirling.tempfiles.directory");
        }

        File tempFile;

        if (customTempDir != null && !customTempDir.isEmpty()) {
            Path tempDir = Path.of(customTempDir);
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
            tempFile = Files.createTempFile(tempDir, "stirling-pdf-", null).toFile();
        } else {
            Path tempDir = Path.of(System.getProperty("java.io.tmpdir"), "stirling-pdf");
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
            tempFile = Files.createTempFile(tempDir, "stirling-pdf-", null).toFile();
        }

        try (InputStream inputStream = multipartFile.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
