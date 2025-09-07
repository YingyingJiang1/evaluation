    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        logger.debug("http request: {} ", request);

        send100Continue(ctx);
        requestHandler.handle(ctx, request);
    }
