    @PostMapping(consumes = "multipart/form-data", value = "/ocr-pdf")
    @Operation(
            summary = "Process a PDF file with OCR",
            description =
                    "This endpoint processes a PDF file using OCR (Optical Character Recognition). "
                            + "Users can specify languages, sidecar, deskew, clean, cleanFinal, ocrType, ocrRenderType, and removeImagesAfter options. "
                            + "Uses OCRmyPDF if available, falls back to Tesseract. Input:PDF Output:PDF Type:SI-Conditional")
    public ResponseEntity<byte[]> processPdfWithOCR(
            @ModelAttribute ProcessPdfWithOcrRequest request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        List<String> selectedLanguages = request.getLanguages();
        Boolean sidecar = request.isSidecar();
        Boolean deskew = request.isDeskew();
        Boolean clean = request.isClean();
        Boolean cleanFinal = request.isCleanFinal();
        String ocrType = request.getOcrType();
        String ocrRenderType = request.getOcrRenderType();
        Boolean removeImagesAfter = request.isRemoveImagesAfter();

        if (selectedLanguages == null || selectedLanguages.isEmpty()) {
            throw ExceptionUtils.createOcrLanguageRequiredException();
        }

        if (!"hocr".equals(ocrRenderType) && !"sandwich".equals(ocrRenderType)) {
            throw new IOException("ocrRenderType wrong");
        }

        // Get available Tesseract languages
        List<String> availableLanguages = getAvailableTesseractLanguages();

        // Validate selected languages
        selectedLanguages =
                selectedLanguages.stream().filter(availableLanguages::contains).toList();

        if (selectedLanguages.isEmpty()) {
            throw ExceptionUtils.createOcrInvalidLanguagesException();
        }

        // Use try-with-resources for proper temp file management
        try (TempFile tempInputFile = new TempFile(tempFileManager, ".pdf");
                TempFile tempOutputFile = new TempFile(tempFileManager, ".pdf")) {

            inputFile.transferTo(tempInputFile.getFile());

            TempFile sidecarTextFile = null;

            try {
                // Use OCRmyPDF if available (no fallback - error if it fails)
                if (isOcrMyPdfEnabled()) {
                    if (sidecar != null && sidecar) {
                        sidecarTextFile = new TempFile(tempFileManager, ".txt");
                    }

                    processWithOcrMyPdf(
                            selectedLanguages,
                            sidecar,
                            deskew,
                            clean,
                            cleanFinal,
                            ocrType,
                            ocrRenderType,
                            removeImagesAfter,
                            tempInputFile.getPath(),
                            tempOutputFile.getPath(),
                            sidecarTextFile != null ? sidecarTextFile.getPath() : null);
                    log.info("OCRmyPDF processing completed successfully");
                }
                // Use Tesseract only if OCRmyPDF is not available
                else if (isTesseractEnabled()) {
                    processWithTesseract(
                            selectedLanguages,
                            ocrType,
                            tempInputFile.getPath(),
                            tempOutputFile.getPath());
                    log.info("Tesseract processing completed successfully");
                } else {
                    throw ExceptionUtils.createOcrToolsUnavailableException();
                }

                // Read the processed PDF file
                byte[] pdfBytes = Files.readAllBytes(tempOutputFile.getPath());

                // Return the OCR processed PDF as a response
                String outputFilename =
                        Filenames.toSimpleFileName(inputFile.getOriginalFilename())
                                        .replaceFirst("[.][^.]+$", "")
                                + "_OCR.pdf";

                if (sidecar != null && sidecar && sidecarTextFile != null) {
                    // Create a zip file containing both the PDF and the text file
                    String outputZipFilename =
                            Filenames.toSimpleFileName(inputFile.getOriginalFilename())
                                            .replaceFirst("[.][^.]+$", "")
                                    + "_OCR.zip";

                    try (TempFile tempZipFile = new TempFile(tempFileManager, ".zip");
                            ZipOutputStream zipOut =
                                    new ZipOutputStream(
                                            Files.newOutputStream(tempZipFile.getPath()))) {

                        // Add PDF file to the zip
                        ZipEntry pdfEntry = new ZipEntry(outputFilename);
                        zipOut.putNextEntry(pdfEntry);
                        zipOut.write(pdfBytes);
                        zipOut.closeEntry();

                        // Add text file to the zip
                        ZipEntry txtEntry = new ZipEntry(outputFilename.replace(".pdf", ".txt"));
                        zipOut.putNextEntry(txtEntry);
                        Files.copy(sidecarTextFile.getPath(), zipOut);
                        zipOut.closeEntry();

                        zipOut.finish();

                        byte[] zipBytes = Files.readAllBytes(tempZipFile.getPath());

                        // Return the zip file containing both the PDF and the text file
                        return WebResponseUtils.bytesToWebResponse(
                                zipBytes, outputZipFilename, MediaType.APPLICATION_OCTET_STREAM);
                    }
                } else {
                    // Return the OCR processed PDF as a response
                    return WebResponseUtils.bytesToWebResponse(pdfBytes, outputFilename);
                }

            } finally {
                // Clean up sidecar temp file if created
                if (sidecarTextFile != null) {
                    try {
                        sidecarTextFile.close();
                    } catch (Exception e) {
                        log.warn("Failed to close sidecar temp file", e);
                    }
                }
            }
        }
    }
