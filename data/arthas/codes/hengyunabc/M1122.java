  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    HttpTtyConnection tmp = conn;
    context = null;
    conn = null;
    if (tmp != null) {
      Consumer<Void> closeHandler = tmp.getCloseHandler();
      if (closeHandler != null) {
        closeHandler.accept(null);
      }
    }
  }
