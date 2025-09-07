    public static byte[] getImageData(BufferedImage image) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        if (dataBuffer instanceof DataBufferByte dataBufferByte) {
            return dataBufferByte.getData();
        } else if (dataBuffer instanceof DataBufferInt dataBufferInt) {
            int[] intData = dataBufferInt.getData();
            ByteBuffer byteBuffer = ByteBuffer.allocate(intData.length * 4);
            byteBuffer.asIntBuffer().put(intData);
            return byteBuffer.array();
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            byte[] data = new byte[width * height * 3];
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    data[index++] = (byte) ((rgb >> 16) & 0xFF); // Red
                    data[index++] = (byte) ((rgb >> 8) & 0xFF); // Green
                    data[index++] = (byte) (rgb & 0xFF); // Blue
                }
            }
            return data;
        }
    }
