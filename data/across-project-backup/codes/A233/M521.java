    private static String decodeQRCode(BufferedImage bufferedImage) {
        LuminanceSource source;

        if (bufferedImage.getRaster().getDataBuffer() instanceof DataBufferByte dataBufferByte) {
            byte[] pixels = dataBufferByte.getData();
            source =
                    new PlanarYUVLuminanceSource(
                            pixels,
                            bufferedImage.getWidth(),
                            bufferedImage.getHeight(),
                            0,
                            0,
                            bufferedImage.getWidth(),
                            bufferedImage.getHeight(),
                            false);
        } else if (bufferedImage.getRaster().getDataBuffer()
                instanceof DataBufferInt dataBufferInt) {
            int[] pixels = dataBufferInt.getData();
            byte[] newPixels = new byte[pixels.length];
            for (int i = 0; i < pixels.length; i++) {
                newPixels[i] = (byte) (pixels[i] & 0xff);
            }
            source =
                    new PlanarYUVLuminanceSource(
                            newPixels,
                            bufferedImage.getWidth(),
                            bufferedImage.getHeight(),
                            0,
                            0,
                            bufferedImage.getWidth(),
                            bufferedImage.getHeight(),
                            false);
        } else {
            throw new IllegalArgumentException(
                    "BufferedImage must have 8-bit gray scale, 24-bit RGB, 32-bit ARGB (packed"
                            + " int), byte gray, or 3-byte/4-byte RGB image data");
        }

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null; // there is no QR code in the image
        }
    }
