    public static void main(String[] args) {
        // Print welcome info
        WelcomeUtil.printNativeAgentWelcomeMsg();

        // Check And Find arthas path
        logger.info("check arthas file path...");
        ArthasHomeHandler.findArthasHome();
        logger.info("check arthas file path success");

        // Read bootstrap config
        logger.info("read input config...");
        NativeAgentBootstrap nativeAgentBootstrap = new NativeAgentBootstrap();
        CLI cli = CLIConfigurator.define(NativeAgentBootstrap.class);
        CommandLine commandLine = cli.parse(Arrays.asList(args));
        try {
            CLIConfigurator.inject(commandLine, nativeAgentBootstrap);
        } catch (Throwable e) {
            logger.error("Missing startup parameter");
            e.printStackTrace();
            System.out.println(usage(cli));
            System.exit(1);
        }
        logger.info("read input config success");

        // Register native agent
        try {
            logger.info("register native agent ...");
            NativeAgentRegistryFactory nativeAgentRegistryFactory = NativeAgentRegistryFactory.getNativeAgentClientRegisterFactory();
            NativeAgentRegistry nativeAgentRegistry = nativeAgentRegistryFactory.getServiceRegistration(nativeAgentBootstrap.getRegistrationType());
            nativeAgentRegistry.registerNativeAgent(nativeAgentBootstrap.getRegistrationAddress()
                    , nativeAgentBootstrap.getIp()
                    , nativeAgentBootstrap.getHttpPortOrDefault() + ":" + nativeAgentBootstrap.getWsPortOrDefault());
            logger.info("register native agent success!");
        } catch (Exception e) {
            logger.error("register native agent failed!");
            e.printStackTrace();
            System.exit(1);
        }

        // Start the websocket server
        int wsPortOrDefault = nativeAgentBootstrap.getWsPortOrDefault();
        Thread wsServerThread = new Thread(() -> {
            logger.info("start the websocket server... ws port:" + wsPortOrDefault);
            try {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    ChannelPipeline p = ch.pipeline();
                                    p.addLast(new HttpRequestDecoder());
                                    p.addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH));
                                    p.addLast(new HttpResponseEncoder());
                                    p.addLast(new WebSocketServerProtocolHandler("/ws"));
                                    p.addLast(new ForwardClientSocketClientHandler());
                                }
                            });
                    ChannelFuture f = b.bind("0.0.0.0", wsPortOrDefault).sync();
                    logger.info("start the websocket server success! ws port:" + wsPortOrDefault);
                    f.channel().closeFuture().sync();
                } finally {
                    logger.info("shutdown websocket server, ws port:{}", wsPortOrDefault);
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            } catch (InterruptedException e) {
                logger.error("failed to start  websocket server, ws port: {}", wsPortOrDefault);
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        wsServerThread.setName("native-agent-ws-server");
        wsServerThread.start();

        // Start the Http server
        int httpPortOrDefault = nativeAgentBootstrap.getHttpPortOrDefault();
        logger.info("start the http server... http port:" + httpPortOrDefault);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH));
                            ch.pipeline().addLast(new HttpRequestHandler());
                        }
                    });
            ChannelFuture f = b.bind("0.0.0.0", httpPortOrDefault).sync();
            logger.info("start the http server success, http port:" + httpPortOrDefault);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("failed to start http server, http port:" + httpPortOrDefault);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("shutdown http server");
        }

    }
