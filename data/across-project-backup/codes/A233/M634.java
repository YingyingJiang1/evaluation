    private void addImageWatermark(
            PDPageContentStream contentStream,
            MultipartFile watermarkImage,
            PDDocument document,
            PDPage page,
            float rotation,
            int widthSpacer,
            int heightSpacer,
            float fontSize)
            throws IOException {

        // Load the watermark image
        BufferedImage image = ImageIO.read(watermarkImage.getInputStream());

        // Compute width based on original aspect ratio
        float aspectRatio = (float) image.getWidth() / (float) image.getHeight();

        // Desired physical height (in PDF points)
        float desiredPhysicalHeight = fontSize;

        // Desired physical width based on the aspect ratio
        float desiredPhysicalWidth = desiredPhysicalHeight * aspectRatio;

        // Convert the BufferedImage to PDImageXObject
        PDImageXObject xobject = LosslessFactory.createFromImage(document, image);

        // Calculate the number of rows and columns for watermarks
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        int watermarkRows =
                (int) ((pageHeight + heightSpacer) / (desiredPhysicalHeight + heightSpacer));
        int watermarkCols =
                (int) ((pageWidth + widthSpacer) / (desiredPhysicalWidth + widthSpacer));

        for (int i = 0; i < watermarkRows; i++) {
            for (int j = 0; j < watermarkCols; j++) {
                float x = j * (desiredPhysicalWidth + widthSpacer);
                float y = i * (desiredPhysicalHeight + heightSpacer);

                // Save the graphics state
                contentStream.saveGraphicsState();

                // Create rotation matrix and rotate
                contentStream.transform(
                        Matrix.getTranslateInstance(
                                x + desiredPhysicalWidth / 2, y + desiredPhysicalHeight / 2));
                contentStream.transform(Matrix.getRotateInstance(Math.toRadians(rotation), 0, 0));
                contentStream.transform(
                        Matrix.getTranslateInstance(
                                -desiredPhysicalWidth / 2, -desiredPhysicalHeight / 2));

                // Draw the image and restore the graphics state
                contentStream.drawImage(xobject, 0, 0, desiredPhysicalWidth, desiredPhysicalHeight);
                contentStream.restoreGraphicsState();
            }
        }
    }
