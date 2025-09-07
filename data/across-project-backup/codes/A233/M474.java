    @PostMapping(value = "/split-by-size-or-count", consumes = "multipart/form-data")
    @Operation(
            summary = "Auto split PDF pages into separate documents based on size or count",
            description =
                    "split PDF into multiple paged documents based on size/count, ie if 20 pages"
                            + " and split into 5, it does 5 documents each 4 pages\r\n"
                            + " if 10MB and each page is 1MB and you enter 2MB then 5 docs each 2MB"
                            + " (rounded so that it accepts 1.9MB but not 2.1MB) Input:PDF"
                            + " Output:ZIP-PDF Type:SISO")
    public ResponseEntity<byte[]> autoSplitPdf(@ModelAttribute SplitPdfBySizeOrCountRequest request)
            throws Exception {

        log.debug("Starting PDF split process with request: {}", request);
        MultipartFile file = request.getFileInput();

        Path zipFile = Files.createTempFile("split_documents", ".zip");
        log.debug("Created temporary zip file: {}", zipFile);

        String filename =
                Filenames.toSimpleFileName(file.getOriginalFilename())
                        .replaceFirst("[.][^.]+$", "");
        log.debug("Base filename for output: {}", filename);

        byte[] data = null;
        try {
            log.debug("Reading input file bytes");
            byte[] pdfBytes = file.getBytes();
            log.debug("Successfully read {} bytes from input file", pdfBytes.length);

            log.debug("Creating ZIP output stream");
            try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFile))) {
                log.debug("Loading PDF document");
                try (PDDocument sourceDocument = pdfDocumentFactory.load(pdfBytes)) {
                    log.debug(
                            "Successfully loaded PDF with {} pages",
                            sourceDocument.getNumberOfPages());

                    int type = request.getSplitType();
                    String value = request.getSplitValue();
                    log.debug("Split type: {}, Split value: {}", type, value);

                    if (type == 0) {
                        log.debug("Processing split by size");
                        long maxBytes = GeneralUtils.convertSizeToBytes(value);
                        log.debug("Max bytes per document: {}", maxBytes);
                        handleSplitBySize(sourceDocument, maxBytes, zipOut, filename);
                    } else if (type == 1) {
                        log.debug("Processing split by page count");
                        int pageCount = Integer.parseInt(value);
                        log.debug("Pages per document: {}", pageCount);
                        handleSplitByPageCount(sourceDocument, pageCount, zipOut, filename);
                    } else if (type == 2) {
                        log.debug("Processing split by document count");
                        int documentCount = Integer.parseInt(value);
                        log.debug("Total number of documents: {}", documentCount);
                        handleSplitByDocCount(sourceDocument, documentCount, zipOut, filename);
                    } else {
                        log.error("Invalid split type: {}", type);
                        throw ExceptionUtils.createIllegalArgumentException(
                                "error.invalidArgument",
                                "Invalid argument: {0}",
                                "split type: " + type);
                    }

                    log.debug("PDF splitting completed successfully");
                } catch (Exception e) {
                    ExceptionUtils.logException("PDF document loading or processing", e);
                    throw e;
                }
            } catch (IOException e) {
                log.error("Error creating or writing to ZIP file", e);
                throw e;
            }

        } catch (Exception e) {
            ExceptionUtils.logException("PDF splitting process", e);
            throw e; // Re-throw to ensure proper error response
        } finally {
            try {
                log.debug("Reading ZIP file data");
                data = Files.readAllBytes(zipFile);
                log.debug("Successfully read {} bytes from ZIP file", data.length);
            } catch (IOException e) {
                log.error("Error reading ZIP file data", e);
            }

            try {
                log.debug("Deleting temporary ZIP file");
                boolean deleted = Files.deleteIfExists(zipFile);
                log.debug("Temporary ZIP file deleted: {}", deleted);
            } catch (IOException e) {
                log.error("Error deleting temporary ZIP file", e);
            }
        }

        log.debug("Returning response with {} bytes of data", data != null ? data.length : 0);
        return WebResponseUtils.bytesToWebResponse(
                data, filename + ".zip", MediaType.APPLICATION_OCTET_STREAM);
    }
