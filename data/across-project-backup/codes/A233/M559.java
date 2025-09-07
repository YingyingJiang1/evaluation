    private void addImageStamp(
            PDPageContentStream contentStream,
            MultipartFile stampImage,
            PDDocument document,
            PDPage page,
            float rotation,
            int position, // 1-9 positioning logic
            float fontSize,
            float overrideX,
            float overrideY,
            float margin)
            throws IOException {

        // Load the stamp image
        BufferedImage image = ImageIO.read(stampImage.getInputStream());

        // Compute width based on original aspect ratio
        float aspectRatio = (float) image.getWidth() / (float) image.getHeight();

        // Desired physical height (in PDF points)
        float desiredPhysicalHeight = fontSize;

        // Desired physical width based on the aspect ratio
        float desiredPhysicalWidth = desiredPhysicalHeight * aspectRatio;

        // Convert the BufferedImage to PDImageXObject
        PDImageXObject xobject = LosslessFactory.createFromImage(document, image);

        PDRectangle pageSize = page.getMediaBox();
        float x, y;

        if (overrideX >= 0 && overrideY >= 0) {
            // Use override values if provided
            x = overrideX;
            y = overrideY;
        } else {
            x = calculatePositionX(pageSize, position, desiredPhysicalWidth, null, 0, null, margin);
            y = calculatePositionY(pageSize, position, fontSize, margin);
        }

        contentStream.saveGraphicsState();
        contentStream.transform(Matrix.getTranslateInstance(x, y));
        contentStream.transform(Matrix.getRotateInstance(Math.toRadians(rotation), 0, 0));
        contentStream.drawImage(xobject, 0, 0, desiredPhysicalWidth, desiredPhysicalHeight);
        contentStream.restoreGraphicsState();
    }
