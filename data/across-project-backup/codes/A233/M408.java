    private static Path unzipAndGetMainHtml(byte[] fileBytes) throws IOException {
        Path tempDirectory = Files.createTempDirectory("unzipped_");
        try (ZipInputStream zipIn =
                ZipSecurity.createHardenedInputStream(new ByteArrayInputStream(fileBytes))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                Path filePath = tempDirectory.resolve(sanitizeZipFilename(entry.getName()));
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath); // Explicitly create the directory structure
                } else {
                    Files.createDirectories(
                            filePath.getParent()); // Create parent directories if they don't exist
                    Files.copy(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }

        // Search for the main HTML file.
        try (Stream<Path> walk = Files.walk(tempDirectory)) {
            List<Path> htmlFiles = walk.filter(file -> file.toString().endsWith(".html")).toList();

            if (htmlFiles.isEmpty()) {
                throw new IOException("No HTML files found in the unzipped directory.");
            }

            // Prioritize 'index.html' if it exists, otherwise use the first .html file
            for (Path htmlFile : htmlFiles) {
                if ("index.html".equals(htmlFile.getFileName().toString())) {
                    return htmlFile;
                }
            }

            return htmlFiles.get(0);
        }
    }
