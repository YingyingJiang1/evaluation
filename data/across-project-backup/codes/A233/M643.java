    private List<Resource> unzip(byte[] data) throws IOException {
        log.info("Unzipping data of length: {}", data.length);
        List<Resource> unzippedFiles = new ArrayList<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ZipInputStream zis = ZipSecurity.createHardenedInputStream(bais)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                final String filename = entry.getName();
                Resource fileResource =
                        new ByteArrayResource(baos.toByteArray()) {

                            @Override
                            public String getFilename() {
                                return filename;
                            }
                        };
                // If the unzipped file is a zip file, unzip it
                if (isZip(baos.toByteArray())) {
                    log.info("File {} is a zip file. Unzipping...", filename);
                    unzippedFiles.addAll(unzip(baos.toByteArray()));
                } else {
                    unzippedFiles.add(fileResource);
                }
            }
        }
        log.info("Unzipping completed. {} files were unzipped.", unzippedFiles.size());
        return unzippedFiles;
    }
