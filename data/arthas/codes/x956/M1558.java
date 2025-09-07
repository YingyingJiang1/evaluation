    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("grpc web proxy handler error", cause);
        ctx.close();
    }
