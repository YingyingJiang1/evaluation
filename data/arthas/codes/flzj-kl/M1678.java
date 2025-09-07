    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof WebSocketFrame) {
            inboundChannel.writeAndFlush(((WebSocketFrame) msg).retain());
        }
    }
