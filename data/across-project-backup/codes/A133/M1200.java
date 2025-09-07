    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.readableBytes() < 3) {
            return;
        }

        if (detectTelnetFuture != null && detectTelnetFuture.isCancellable()) {
            detectTelnetFuture.cancel(false);
        }

        byte[] bytes = new byte[3];
        in.getBytes(0, bytes);
        String httpHeader = new String(bytes);

        ChannelPipeline pipeline = ctx.pipeline();
        if (!"GET".equalsIgnoreCase(httpHeader)) { // telnet
            channelGroup.add(ctx.channel());
            TelnetChannelHandler handler = new TelnetChannelHandler(handlerFactory);
            pipeline.addLast(handler);
            ctx.fireChannelActive(); // trigger TelnetChannelHandler init
        } else {
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH));
            pipeline.addLast(new BasicHttpAuthenticatorHandler(httpSessionManager));
            pipeline.addLast(workerGroup, "HttpRequestHandler", new HttpRequestHandler(ArthasConstants.DEFAULT_WEBSOCKET_PATH));
            pipeline.addLast(new WebSocketServerProtocolHandler(ArthasConstants.DEFAULT_WEBSOCKET_PATH, null, false, ArthasConstants.MAX_HTTP_CONTENT_LENGTH, false, true));
            pipeline.addLast(new IdleStateHandler(0, 0, ArthasConstants.WEBSOCKET_IDLE_SECONDS));
            pipeline.addLast(new TtyWebSocketFrameHandler(channelGroup, ttyConnectionFactory));
            ctx.fireChannelActive();
        }
        pipeline.remove(this);
        ctx.fireChannelRead(in);
    }
