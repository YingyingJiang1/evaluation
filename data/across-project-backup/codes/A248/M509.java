    @PostMapping(consumes = "multipart/form-data", value = "/eml/pdf")
    @Operation(
            summary = "Convert EML to PDF",
            description =
                    "This endpoint converts EML (email) files to PDF format with extensive"
                            + " customization options. Features include font settings, image constraints, display modes, attachment handling,"
                            + " and HTML debug output. Input: EML file, Output: PDF"
                            + " or HTML file. Type: SISO")
    public ResponseEntity<byte[]> convertEmlToPdf(@ModelAttribute EmlToPdfRequest request) {

        MultipartFile inputFile = request.getFileInput();
        String originalFilename = inputFile.getOriginalFilename();

        // Validate input
        if (inputFile.isEmpty()) {
            log.error("No file provided for EML to PDF conversion.");
            return ResponseEntity.badRequest()
                    .body("No file provided".getBytes(StandardCharsets.UTF_8));
        }

        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            log.error("Filename is null or empty.");
            return ResponseEntity.badRequest()
                    .body("Please provide a valid filename".getBytes(StandardCharsets.UTF_8));
        }

        // Validate file type - support EML
        String lowerFilename = originalFilename.toLowerCase();
        if (!lowerFilename.endsWith(".eml")) {
            log.error("Invalid file type for EML to PDF: {}", originalFilename);
            return ResponseEntity.badRequest()
                    .body("Please upload a valid EML file".getBytes(StandardCharsets.UTF_8));
        }

        String baseFilename = Filenames.toSimpleFileName(originalFilename); // Use Filenames utility

        try {
            byte[] fileBytes = inputFile.getBytes();

            if (request.isDownloadHtml()) {
                try {
                    String htmlContent = EmlToPdf.convertEmlToHtml(fileBytes, request);
                    log.info("Successfully converted EML to HTML: {}", originalFilename);
                    return WebResponseUtils.bytesToWebResponse(
                            htmlContent.getBytes(StandardCharsets.UTF_8),
                            baseFilename + ".html",
                            MediaType.TEXT_HTML);
                } catch (IOException | IllegalArgumentException e) {
                    log.error("HTML conversion failed for {}", originalFilename, e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                    ("HTML conversion failed: " + e.getMessage())
                                            .getBytes(StandardCharsets.UTF_8));
                }
            }

            // Convert EML to PDF with enhanced options
            try {
                byte[] pdfBytes =
                        EmlToPdf.convertEmlToPdf(
                                runtimePathConfig
                                        .getWeasyPrintPath(), // Use configured WeasyPrint path
                                request,
                                fileBytes,
                                originalFilename,
                                false,
                                pdfDocumentFactory,
                                tempFileManager);

                if (pdfBytes == null || pdfBytes.length == 0) {
                    log.error("PDF conversion failed - empty output for {}", originalFilename);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                    "PDF conversion failed - empty output"
                                            .getBytes(StandardCharsets.UTF_8));
                }
                log.info("Successfully converted EML to PDF: {}", originalFilename);
                return WebResponseUtils.bytesToWebResponse(
                        pdfBytes, baseFilename + ".pdf", MediaType.APPLICATION_PDF);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("EML to PDF conversion was interrupted for {}", originalFilename, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Conversion was interrupted".getBytes(StandardCharsets.UTF_8));
            } catch (IllegalArgumentException e) {
                String errorMessage = buildErrorMessage(e, originalFilename);
                log.error(
                        "EML to PDF conversion failed for {}: {}",
                        originalFilename,
                        errorMessage,
                        e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(errorMessage.getBytes(StandardCharsets.UTF_8));
            } catch (RuntimeException e) {
                String errorMessage = buildErrorMessage(e, originalFilename);
                log.error(
                        "EML to PDF conversion failed for {}: {}",
                        originalFilename,
                        errorMessage,
                        e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(errorMessage.getBytes(StandardCharsets.UTF_8));
            }

        } catch (IOException e) {
            log.error("File processing error for EML to PDF: {}", originalFilename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error".getBytes(StandardCharsets.UTF_8));
        }
    }
