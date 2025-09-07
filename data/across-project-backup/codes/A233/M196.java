    private void ensureDirectoriesExist() {
        try {
            ApplicationProperties.TempFileManagement tempFiles =
                    applicationProperties.getSystem().getTempFileManagement();

            // Create the main temp directory
            String customTempDirectory = tempFiles.getBaseTmpDir();
            if (customTempDirectory != null && !customTempDirectory.isEmpty()) {
                Path tempDir = Path.of(customTempDirectory);
                if (!Files.exists(tempDir)) {
                    Files.createDirectories(tempDir);
                    log.info("Created temp directory: {}", tempDir);
                }
            }

            // Create LibreOffice temp directory
            String libreOfficeTempDir = tempFiles.getLibreofficeDir();
            if (libreOfficeTempDir != null && !libreOfficeTempDir.isEmpty()) {
                Path loTempDir = Path.of(libreOfficeTempDir);
                if (!Files.exists(loTempDir)) {
                    Files.createDirectories(loTempDir);
                    log.info("Created LibreOffice temp directory: {}", loTempDir);
                }
            }
        } catch (IOException e) {
            log.error("Error creating temp directories", e);
        }
    }
