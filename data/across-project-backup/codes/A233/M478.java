    private void saveDocumentToZip(
            PDDocument document, ZipOutputStream zipOut, String baseFilename, int index)
            throws IOException {
        log.debug("Starting saveDocumentToZip for document part {}", index);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            log.debug("Saving document part {} to byte array", index);
            document.save(outStream);
            log.debug("Successfully saved document part {} ({} bytes)", index, outStream.size());
        } catch (Exception e) {
            log.error("Error saving document part {} to byte array", index, e);
            throw ExceptionUtils.createFileProcessingException("split", e);
        }

        try {
            log.debug("Closing document part {}", index);
            document.close();
            log.debug("Successfully closed document part {}", index);
        } catch (Exception e) {
            log.error("Error closing document part {}", index, e);
            // Continue despite close error
        }

        try {
            // Create a new zip entry
            String entryName = baseFilename + "_" + index + ".pdf";
            log.debug("Creating ZIP entry: {}", entryName);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = outStream.toByteArray();
            log.debug("Writing {} bytes to ZIP entry", bytes.length);
            zipOut.write(bytes);

            log.debug("Closing ZIP entry");
            zipOut.closeEntry();
            log.debug("Successfully added document part {} to ZIP", index);
        } catch (Exception e) {
            log.error("Error adding document part {} to ZIP", index, e);
            throw ExceptionUtils.createFileProcessingException("split", e);
        }
    }
