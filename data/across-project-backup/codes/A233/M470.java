    @PostMapping(value = "/scale-pages", consumes = "multipart/form-data")
    @Operation(
            summary = "Change the size of a PDF page/document",
            description =
                    "This operation takes an input PDF file and the size to scale the pages to in"
                            + " the output PDF file. Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> scalePages(@ModelAttribute ScalePagesRequest request)
            throws IOException {
        MultipartFile file = request.getFileInput();
        String targetPDRectangle = request.getPageSize();
        float scaleFactor = request.getScaleFactor();

        PDDocument sourceDocument = pdfDocumentFactory.load(file);
        PDDocument outputDocument =
                pdfDocumentFactory.createNewDocumentBasedOnOldDocument(sourceDocument);

        PDRectangle targetSize = getTargetSize(targetPDRectangle, sourceDocument);

        int totalPages = sourceDocument.getNumberOfPages();
        for (int i = 0; i < totalPages; i++) {
            PDPage sourcePage = sourceDocument.getPage(i);
            PDRectangle sourceSize = sourcePage.getMediaBox();

            float scaleWidth = targetSize.getWidth() / sourceSize.getWidth();
            float scaleHeight = targetSize.getHeight() / sourceSize.getHeight();
            float scale = Math.min(scaleWidth, scaleHeight) * scaleFactor;

            PDPage newPage = new PDPage(targetSize);
            outputDocument.addPage(newPage);

            PDPageContentStream contentStream =
                    new PDPageContentStream(
                            outputDocument,
                            newPage,
                            PDPageContentStream.AppendMode.APPEND,
                            true,
                            true);

            float x = (targetSize.getWidth() - sourceSize.getWidth() * scale) / 2;
            float y = (targetSize.getHeight() - sourceSize.getHeight() * scale) / 2;

            contentStream.saveGraphicsState();
            contentStream.transform(Matrix.getTranslateInstance(x, y));
            contentStream.transform(Matrix.getScaleInstance(scale, scale));

            LayerUtility layerUtility = new LayerUtility(outputDocument);
            PDFormXObject form = layerUtility.importPageAsForm(sourceDocument, i);
            contentStream.drawForm(form);

            contentStream.restoreGraphicsState();
            contentStream.close();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outputDocument.save(baos);
        outputDocument.close();
        sourceDocument.close();

        return WebResponseUtils.bytesToWebResponse(
                baos.toByteArray(),
                Filenames.toSimpleFileName(file.getOriginalFilename()).replaceFirst("[.][^.]+$", "")
                        + "_scaled.pdf");
    }
