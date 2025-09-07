    public List<PDDocument> splitPdfPages(
            PDDocument document, int horizontalDivisions, int verticalDivisions)
            throws IOException {
        List<PDDocument> splitDocuments = new ArrayList<>();

        for (PDPage originalPage : document.getPages()) {
            PDRectangle originalMediaBox = originalPage.getMediaBox();
            float width = originalMediaBox.getWidth();
            float height = originalMediaBox.getHeight();
            float subPageWidth = width / horizontalDivisions;
            float subPageHeight = height / verticalDivisions;

            LayerUtility layerUtility = new LayerUtility(document);

            for (int i = 0; i < horizontalDivisions; i++) {
                for (int j = 0; j < verticalDivisions; j++) {
                    PDDocument subDoc = new PDDocument();
                    PDPage subPage = new PDPage(new PDRectangle(subPageWidth, subPageHeight));
                    subDoc.addPage(subPage);

                    PDFormXObject form =
                            layerUtility.importPageAsForm(
                                    document, document.getPages().indexOf(originalPage));

                    try (PDPageContentStream contentStream =
                            new PDPageContentStream(
                                    subDoc, subPage, AppendMode.APPEND, true, true)) {
                        // Set clipping area and position
                        float translateX = -subPageWidth * i;

                        // float translateY = height - subPageHeight * (verticalDivisions - j);
                        float translateY = -subPageHeight * (verticalDivisions - 1 - j);

                        contentStream.saveGraphicsState();
                        contentStream.addRect(0, 0, subPageWidth, subPageHeight);
                        contentStream.clip();
                        contentStream.transform(new Matrix(1, 0, 0, 1, translateX, translateY));

                        // Draw the form
                        contentStream.drawForm(form);
                        contentStream.restoreGraphicsState();
                    }

                    splitDocuments.add(subDoc);
                }
            }
        }

        return splitDocuments;
    }
