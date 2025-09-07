    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) {
        if (evt.equals(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE)) {
            try {
                connectLocalServer(ctx);
            } catch (Throwable e) {
                logger.error("ForwardClientSocketClientHandler connect local arthas server error", e);
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }
