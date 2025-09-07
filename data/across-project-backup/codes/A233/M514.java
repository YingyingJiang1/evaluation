    @PostMapping(consumes = "multipart/form-data", value = "/html/pdf")
    @Operation(
            summary = "Convert an HTML or ZIP (containing HTML and CSS) to PDF",
            description =
                    "This endpoint takes an HTML or ZIP file input and converts it to a PDF format."
                            + " Input:HTML Output:PDF Type:SISO")
    public ResponseEntity<byte[]> HtmlToPdf(@ModelAttribute HTMLToPdfRequest request)
            throws Exception {
        MultipartFile fileInput = request.getFileInput();

        if (fileInput == null) {
            throw ExceptionUtils.createHtmlFileRequiredException();
        }

        String originalFilename = Filenames.toSimpleFileName(fileInput.getOriginalFilename());
        if (originalFilename == null
                || (!originalFilename.endsWith(".html") && !originalFilename.endsWith(".zip"))) {
            throw ExceptionUtils.createIllegalArgumentException(
                    "error.fileFormatRequired", "File must be in {0} format", ".html or .zip");
        }

        boolean disableSanitize =
                Boolean.TRUE.equals(applicationProperties.getSystem().getDisableSanitize());

        byte[] pdfBytes =
                FileToPdf.convertHtmlToPdf(
                        runtimePathConfig.getWeasyPrintPath(),
                        request,
                        fileInput.getBytes(),
                        originalFilename,
                        disableSanitize,
                        tempFileManager);

        pdfBytes = pdfDocumentFactory.createNewBytesBasedOnOldDocument(pdfBytes);

        String outputFilename =
                originalFilename.replaceFirst("[.][^.]+$", "")
                        + ".pdf"; // Remove file extension and append .pdf

        return WebResponseUtils.bytesToWebResponse(pdfBytes, outputFilename);
    }
