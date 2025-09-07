    private PDImageXObject compressImage(
            PDDocument doc,
            PDImageXObject originalImage,
            int originalSize,
            double scaleFactor,
            float jpegQuality,
            boolean convertToGrayscale)
            throws IOException {

        // Process and compress the image
        BufferedImage processedImage =
                processAndCompressImage(
                        originalImage, scaleFactor, jpegQuality, convertToGrayscale);

        if (processedImage == null) {
            return null;
        }

        // Convert to bytes for storage
        byte[] compressedData = convertToBytes(processedImage, jpegQuality);

        // Check if compression is beneficial
        if (compressedData.length < originalSize || convertToGrayscale) {
            // Create a compressed version
            return PDImageXObject.createFromByteArray(
                    doc, compressedData, originalImage.getCOSObject().toString());
        }

        return null;
    }
