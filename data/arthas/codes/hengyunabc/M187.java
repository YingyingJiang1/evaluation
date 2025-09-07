    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        tunnelClient.setConnected(false);
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                logger.error("try to reconnect to tunnel server, uri: {}", tunnelClient.getTunnelServerUrl());
                try {
                    tunnelClient.connect(true);
                } catch (Throwable e) {
                    logger.error("reconnect error", e);
                }
            }
        }, tunnelClient.getReconnectDelay(), TimeUnit.SECONDS);
    }
