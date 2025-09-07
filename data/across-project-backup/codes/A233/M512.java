    @PostMapping(consumes = "multipart/form-data", value = "/pdf/text")
    @Operation(
            summary = "Convert PDF to Text or RTF format",
            description =
                    "This endpoint converts a given PDF file to Text or RTF format. Input:PDF"
                            + " Output:TXT Type:SISO")
    public ResponseEntity<byte[]> processPdfToRTForTXT(
            @ModelAttribute PdfToTextOrRTFRequest request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        String outputFormat = request.getOutputFormat();
        if ("txt".equals(request.getOutputFormat())) {
            try (PDDocument document = pdfDocumentFactory.load(inputFile)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                return WebResponseUtils.bytesToWebResponse(
                        text.getBytes(),
                        Filenames.toSimpleFileName(inputFile.getOriginalFilename())
                                        .replaceFirst("[.][^.]+$", "")
                                + ".txt",
                        MediaType.TEXT_PLAIN);
            }
        } else {
            PDFToFile pdfToFile = new PDFToFile();
            return pdfToFile.processPdfToOfficeFormat(inputFile, outputFormat, "writer_pdf_import");
        }
    }
