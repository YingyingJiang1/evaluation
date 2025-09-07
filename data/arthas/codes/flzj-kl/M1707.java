    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof ClientHandshakeStateEvent) {
            if (evt.equals(ClientHandshakeStateEvent.HANDSHAKE_COMPLETE)) {
                handshakeFuture.setSuccess();
            }
        }
    }
