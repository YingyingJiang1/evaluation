    @Override
    protected void write(byte[] buffer) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(buffer);
        if (context != null) {
            context.writeAndFlush(new TextWebSocketFrame(byteBuf));
        }
    }
