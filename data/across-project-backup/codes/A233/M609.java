    @PostMapping(value = "/auto-redact", consumes = "multipart/form-data")
    @Operation(
            summary = "Redacts listOfText in a PDF document",
            description =
                    "This operation takes an input PDF file and redacts the provided listOfText."
                            + " Input:PDF, Output:PDF, Type:SISO")
    public ResponseEntity<byte[]> redactPdf(@ModelAttribute RedactPdfRequest request)
            throws Exception {
        MultipartFile file = request.getFileInput();
        String listOfTextString = request.getListOfText();
        boolean useRegex = Boolean.TRUE.equals(request.getUseRegex());
        boolean wholeWordSearchBool = Boolean.TRUE.equals(request.getWholeWordSearch());
        String colorString = request.getRedactColor();
        float customPadding = request.getCustomPadding();
        boolean convertPDFToImage = Boolean.TRUE.equals(request.getConvertPDFToImage());

        String[] listOfText = listOfTextString.split("\n");
        PDDocument document = pdfDocumentFactory.load(file);

        Color redactColor;
        try {
            if (!colorString.startsWith("#")) {
                colorString = "#" + colorString;
            }
            redactColor = Color.decode(colorString);
        } catch (NumberFormatException e) {
            log.warn("Invalid color string provided. Using default color BLACK for redaction.");
            redactColor = Color.BLACK;
        }

        for (String text : listOfText) {
            text = text.trim();
            TextFinder textFinder = new TextFinder(text, useRegex, wholeWordSearchBool);
            List<PDFText> foundTexts = textFinder.getTextLocations(document);
            redactFoundText(document, foundTexts, customPadding, redactColor);
        }

        if (convertPDFToImage) {
            PDDocument convertedPdf = PdfUtils.convertPdfToPdfImage(document);
            document.close();
            document = convertedPdf;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        byte[] pdfContent = baos.toByteArray();
        return WebResponseUtils.bytesToWebResponse(
                pdfContent,
                Filenames.toSimpleFileName(file.getOriginalFilename()).replaceFirst("[.][^.]+$", "")
                        + "_redacted.pdf");
    }
