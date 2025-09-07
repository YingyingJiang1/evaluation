    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest && !isWebSocketUpgrade((FullHttpRequest) msg)) {
            httpRequestHandler.handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        if (msg instanceof FullHttpRequest && isWebSocketUpgrade((FullHttpRequest) msg)) {
            wsRequestHandler.handleWebSocketUpgrade(ctx, (FullHttpRequest) msg);
        }
        if (msg instanceof WebSocketFrame) {
            wsRequestHandler.handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }
