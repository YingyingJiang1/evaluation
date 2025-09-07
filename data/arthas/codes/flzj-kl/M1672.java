    private void connectToDestinationServer(ChannelHandlerContext ctx, WebSocketFrame frame) {
        String nativeAgentAddress = (String) ctx.channel().attr(AttributeKey.valueOf("nativeAgentAddress")).get();
        Bootstrap b = new Bootstrap();
        b.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec());
                        p.addLast(new HttpObjectAggregator(65536));
                        p.addLast(new WebSocketClientProtocolHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        URI.create("ws://"+ nativeAgentAddress +"/ws"),
                                        WebSocketVersion.V13, null, false, new DefaultHttpHeaders())));
                        p.addLast(new WebSocketClientHandler(ctx.channel()));
                    }
                });
        String[] addressSplit = nativeAgentAddress.split(":");
        ChannelFuture f = b.connect(addressSplit[0], Integer.parseInt(addressSplit[1]));
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                Channel outboundChannel = future.channel();
                channelMappings.put(ctx.channel(), outboundChannel);
                forwardWebSocketFrame(frame, outboundChannel);
            } else {
                logger.error("Failed to connect to destination server", future.cause());
                ctx.close();
            }
        });
    }
