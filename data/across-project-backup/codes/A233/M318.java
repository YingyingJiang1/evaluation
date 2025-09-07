    public static byte[] overlayImage(
            CustomPDFDocumentFactory pdfDocumentFactory,
            byte[] pdfBytes,
            byte[] imageBytes,
            float x,
            float y,
            boolean everyPage)
            throws IOException {

        PDDocument document = pdfDocumentFactory.load(pdfBytes);

        // Get the first page of the PDF
        int pages = document.getNumberOfPages();
        for (int i = 0; i < pages; i++) {
            PDPage page = document.getPage(i);
            try (PDPageContentStream contentStream =
                    new PDPageContentStream(
                            document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                // Create an image object from the image bytes
                PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, "");
                // Draw the image onto the page at the specified x and y coordinates
                contentStream.drawImage(image, x, y);
                log.info("Image successfully overlayed onto PDF");
                if (!everyPage && i == 0) {
                    break;
                }
            } catch (IOException e) {
                // Log an error message if there is an issue overlaying the image onto the PDF
                log.error("Error overlaying image onto PDF", e);
                throw e;
            }
        }
        // Create a ByteArrayOutputStream to save the PDF to
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        log.info("PDF successfully saved to byte array");
        return baos.toByteArray();
    }
