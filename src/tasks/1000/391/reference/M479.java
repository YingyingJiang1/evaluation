    @PostMapping(consumes = "multipart/form-data", value = "/pdf-to-single-page")
    @Operation(
            summary = "Convert a multi-page PDF into a single long page PDF",
            description =
                    "This endpoint converts a multi-page PDF document into a single paged PDF"
                            + " document. The width of the single page will be same as the input's"
                            + " width, but the height will be the sum of all the pages' heights."
                            + " Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> pdfToSinglePage(@ModelAttribute PDFFile request)
            throws IOException {

        // Load the source document
        PDDocument sourceDocument = pdfDocumentFactory.load(request);

        // Calculate total height and max width
        float totalHeight = 0;
        float maxWidth = 0;
        for (PDPage page : sourceDocument.getPages()) {
            PDRectangle pageSize = page.getMediaBox();
            totalHeight += pageSize.getHeight();
            maxWidth = Math.max(maxWidth, pageSize.getWidth());
        }

        // Create new document and page with calculated dimensions
        PDDocument newDocument =
                pdfDocumentFactory.createNewDocumentBasedOnOldDocument(sourceDocument);
        PDPage newPage = new PDPage(new PDRectangle(maxWidth, totalHeight));
        newDocument.addPage(newPage);

        // Initialize the content stream of the new page
        PDPageContentStream contentStream = new PDPageContentStream(newDocument, newPage);
        contentStream.close();

        LayerUtility layerUtility = new LayerUtility(newDocument);
        float yOffset = totalHeight;

        // For each page, copy its content to the new page at the correct offset
        int pageIndex = 0;
        for (PDPage page : sourceDocument.getPages()) {
            PDFormXObject form = layerUtility.importPageAsForm(sourceDocument, pageIndex);
            AffineTransform af =
                    AffineTransform.getTranslateInstance(
                            0, yOffset - page.getMediaBox().getHeight());
            layerUtility.wrapInSaveRestore(newPage);
            String defaultLayerName = "Layer" + pageIndex;
            layerUtility.appendFormAsLayer(newPage, form, af, defaultLayerName);
            yOffset -= page.getMediaBox().getHeight();
            pageIndex++;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newDocument.save(baos);
        newDocument.close();
        sourceDocument.close();

        byte[] result = baos.toByteArray();
        return WebResponseUtils.bytesToWebResponse(
                result,
                request.getFileInput().getOriginalFilename().replaceFirst("[.][^.]+$", "")
                        + "_singlePage.pdf");
    }
