    private void redactFoundText(
            PDDocument document, List<PDFText> blocks, float customPadding, Color redactColor)
            throws IOException {
        var allPages = document.getDocumentCatalog().getPages();

        for (PDFText block : blocks) {
            var page = allPages.get(block.getPageIndex());
            PDPageContentStream contentStream =
                    new PDPageContentStream(
                            document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setNonStrokingColor(redactColor);
            float padding = (block.getY2() - block.getY1()) * 0.3f + customPadding;
            PDRectangle pageBox = page.getBBox();
            contentStream.addRect(
                    block.getX1(),
                    pageBox.getHeight() - block.getY1() - padding,
                    block.getX2() - block.getX1(),
                    block.getY2() - block.getY1() + 2 * padding);
            contentStream.fill();
            contentStream.close();
        }
    }
