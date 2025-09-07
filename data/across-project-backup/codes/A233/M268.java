    public Path registerLibreOfficeTempDir() throws IOException {
        ApplicationProperties.TempFileManagement tempFiles =
                applicationProperties.getSystem().getTempFileManagement();
        Path loTempDir;
        String libreOfficeTempDir = tempFiles.getLibreofficeDir();
        String customTempDirectory = tempFiles.getBaseTmpDir();

        // First check if explicitly configured
        if (libreOfficeTempDir != null && !libreOfficeTempDir.isEmpty()) {
            loTempDir = Path.of(libreOfficeTempDir);
        }
        // Next check if we have a custom temp directory
        else if (customTempDirectory != null && !customTempDirectory.isEmpty()) {
            loTempDir = Path.of(customTempDirectory, "libreoffice");
        }
        // Fall back to system temp dir with our application prefix
        else {
            loTempDir = Path.of(System.getProperty("java.io.tmpdir"), "stirling-pdf-libreoffice");
        }

        if (!Files.exists(loTempDir)) {
            Files.createDirectories(loTempDir);
        }

        return registry.registerDirectory(loTempDir);
    }
