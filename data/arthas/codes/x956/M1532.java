    @Override
    public HttpContent readChunk(ByteBufAllocator allocator) throws Exception {
        if (input.isEndOfInput()) {
            return null;
        } else {
            ByteBuf buf = input.readChunk(allocator);
            if (buf == null) {
                return null;
            }
            return new DefaultHttpContent(buf);
        }
    }
