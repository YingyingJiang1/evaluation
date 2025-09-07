    @PostMapping(consumes = "multipart/form-data", value = "/sanitize-pdf")
    @Operation(
            summary = "Sanitize a PDF file",
            description =
                    "This endpoint processes a PDF file and removes specific elements based on the"
                            + " provided options. Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> sanitizePDF(@ModelAttribute SanitizePdfRequest request)
            throws IOException {
        MultipartFile inputFile = request.getFileInput();
        boolean removeJavaScript = Boolean.TRUE.equals(request.getRemoveJavaScript());
        boolean removeEmbeddedFiles = Boolean.TRUE.equals(request.getRemoveEmbeddedFiles());
        boolean removeXMPMetadata = Boolean.TRUE.equals(request.getRemoveXMPMetadata());
        boolean removeMetadata = Boolean.TRUE.equals(request.getRemoveMetadata());
        boolean removeLinks = Boolean.TRUE.equals(request.getRemoveLinks());
        boolean removeFonts = Boolean.TRUE.equals(request.getRemoveFonts());

        PDDocument document = pdfDocumentFactory.load(inputFile, true);
        if (removeJavaScript) {
            sanitizeJavaScript(document);
        }

        if (removeEmbeddedFiles) {
            sanitizeEmbeddedFiles(document);
        }

        if (removeXMPMetadata) {
            sanitizeXMPMetadata(document);
        }

        if (removeMetadata) {
            sanitizeDocumentInfoMetadata(document);
        }

        if (removeLinks) {
            sanitizeLinks(document);
        }

        if (removeFonts) {
            sanitizeFonts(document);
        }

        return WebResponseUtils.pdfDocToWebResponse(
                document,
                Filenames.toSimpleFileName(inputFile.getOriginalFilename())
                                .replaceFirst("[.][^.]+$", "")
                        + "_sanitized.pdf");
    }
