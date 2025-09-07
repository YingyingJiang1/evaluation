    @Override
    protected void initChannel(LocalChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH));
        pipeline.addLast(workerGroup, "HttpRequestHandler", new HttpRequestHandler(ArthasConstants.DEFAULT_WEBSOCKET_PATH));
        pipeline.addLast(new WebSocketServerProtocolHandler(ArthasConstants.DEFAULT_WEBSOCKET_PATH, null, false, ArthasConstants.MAX_HTTP_CONTENT_LENGTH, false, true));
        pipeline.addLast(new IdleStateHandler(0, 0, ArthasConstants.WEBSOCKET_IDLE_SECONDS));
        pipeline.addLast(new TtyWebSocketFrameHandler(group, handler));
    }
