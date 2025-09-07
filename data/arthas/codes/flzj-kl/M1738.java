    private ByteBuf readInputStream(InputStream is) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        byte[] tmp = new byte[1024];
        int length;
        while ((length = is.read(tmp)) != -1) {
            buffer.writeBytes(tmp, 0, length);
        }
        is.close();
        return buffer;
    }
