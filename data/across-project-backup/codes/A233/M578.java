    @PostMapping(consumes = "multipart/form-data", value = "/flatten")
    @Operation(
            summary = "Flatten PDF form fields or full page",
            description =
                    "Flattening just PDF form fields or converting each page to images to make text"
                            + " unselectable. Input:PDF, Output:PDF. Type:SISO")
    public ResponseEntity<byte[]> flatten(@ModelAttribute FlattenRequest request) throws Exception {
        MultipartFile file = request.getFileInput();

        PDDocument document = pdfDocumentFactory.load(file);
        Boolean flattenOnlyForms = request.getFlattenOnlyForms();

        if (Boolean.TRUE.equals(flattenOnlyForms)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                acroForm.flatten();
            }
            return WebResponseUtils.pdfDocToWebResponse(
                    document, Filenames.toSimpleFileName(file.getOriginalFilename()));
        } else {
            // flatten whole page aka convert each page to image and readd it (making text
            // unselectable)
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            PDDocument newDocument =
                    pdfDocumentFactory.createNewDocumentBasedOnOldDocument(document);
            int numPages = document.getNumberOfPages();
            for (int i = 0; i < numPages; i++) {
                try {
                    BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                    PDPage page = new PDPage();
                    page.setMediaBox(document.getPage(i).getMediaBox());
                    newDocument.addPage(page);
                    try (PDPageContentStream contentStream =
                            new PDPageContentStream(newDocument, page)) {
                        PDImageXObject pdImage = JPEGFactory.createFromImage(newDocument, image);
                        float pageWidth = page.getMediaBox().getWidth();
                        float pageHeight = page.getMediaBox().getHeight();

                        contentStream.drawImage(pdImage, 0, 0, pageWidth, pageHeight);
                    }
                } catch (IOException e) {
                    log.error("exception", e);
                }
            }
            return WebResponseUtils.pdfDocToWebResponse(
                    newDocument, Filenames.toSimpleFileName(file.getOriginalFilename()));
        }
    }
