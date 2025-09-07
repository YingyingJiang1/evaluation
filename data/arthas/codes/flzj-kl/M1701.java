    private void connectLocalServer(final ChannelHandlerContext ctx) throws InterruptedException, URISyntaxException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // Create the Bootstrap
        Bootstrap bootstrap = new Bootstrap();
        LocalFrameHandler localFrameHandler = new LocalFrameHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH));
                        pipeline.addLast(new WebSocketClientProtocolHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        new URI("ws://127.0.0.1:" + NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT + "/ws"),
                                        WebSocketVersion.V13, null, false, null
                                )
                        ));
                        pipeline.addLast(localFrameHandler);
                    }
                });

        // Connect to arthas server
        Channel arthasChannel = bootstrap.connect("127.0.0.1", NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT).sync().channel();

        this.handshakeFuture = localFrameHandler.handshakeFuture();

        handshakeFuture.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                ChannelPipeline pipeline = future.channel().pipeline();
                pipeline.remove(localFrameHandler);
                pipeline.addLast(new RelayHandler(ctx.channel()));
            }
        });

        handshakeFuture.sync();
        ctx.pipeline().remove(ForwardClientSocketClientHandler.this);
        ctx.pipeline().addLast(new RelayHandler(arthasChannel));

    }
