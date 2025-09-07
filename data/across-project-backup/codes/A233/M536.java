    private byte[] convertToBytes(BufferedImage scaledImage, float jpegQuality) throws IOException {
        String format = scaledImage.getColorModel().hasAlpha() ? "png" : "jpeg";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if ("jpeg".equals(format)) {
            // Get the best available JPEG writer
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = writers.next();

            JPEGImageWriteParam param = (JPEGImageWriteParam) writer.getDefaultWriteParam();

            // Set compression parameters
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(jpegQuality);
            param.setOptimizeHuffmanTables(true); // Better compression
            param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT); // Progressive scanning

            // Write compressed image
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(scaledImage, null, null), param);
            }
            writer.dispose();
        } else {
            ImageIO.write(scaledImage, format, outputStream);
        }

        return outputStream.toByteArray();
    }
