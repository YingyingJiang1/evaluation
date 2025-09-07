  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
      ctx.pipeline().remove(HttpRequestHandler.class);
      group.add(ctx.channel());
      conn = new ExtHttpTtyConnection(context);
      handler.accept(conn);
    } else if (evt instanceof IdleStateEvent) {
      ctx.writeAndFlush(new PingWebSocketFrame());
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }
