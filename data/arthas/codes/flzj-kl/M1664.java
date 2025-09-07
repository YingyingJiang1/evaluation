    public static void main(String[] args) {
        // Print welcome message
        WelcomeUtil.printProxyWelcomeMsg();

        // Startup parameter analysis
        logger.info("read input config...");
        NativeAgentProxyBootstrap nativeAgentProxyBootstrap = new NativeAgentProxyBootstrap();
        CLI cli = CLIConfigurator.define(NativeAgentProxyBootstrap.class);
        CommandLine commandLine = cli.parse(Arrays.asList(args));
        try {
            CLIConfigurator.inject(commandLine, nativeAgentProxyBootstrap);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("read input success!");


        // Register native agent proxy
        try {
            logger.info("register native agent proxy...");
            NativeAgentProxyRegistryFactory registerFactory = NativeAgentProxyRegistryFactory.getNativeAgentProxyRegistryFactory();
            NativeAgentProxyRegistry proxyRegistry = registerFactory.getNativeAgentProxyRegistry(nativeAgentProxyBootstrap.getManagementRegistrationType());
            String registerAddress = nativeAgentProxyBootstrap.getIp() + ":" + nativeAgentProxyBootstrap.getPortOrDefault();
            proxyRegistry.register(nativeAgentProxyBootstrap.getManagementRegistrationAddress()
                    , registerAddress
                    , registerAddress);
            logger.info("register native agent client success!");
        } catch (Exception e) {
            logger.error("register native agent client failed!");
            e.printStackTrace();
            System.exit(1);
        }

        // Start the http/ws server
        logger.info("start the server... port:{}", nativeAgentProxyBootstrap.getPortOrDefault());
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
                            ch.pipeline().addLast(new HttpObjectAggregator(NativeAgentConstants.MAX_HTTP_CONTENT_LENGTH));
                            ch.pipeline().addLast(new RequestHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                        }
                    });
            ChannelFuture f = b.bind(nativeAgentProxyBootstrap.getPortOrDefault()).sync();
            logger.info("start the http server success! port:{}", nativeAgentProxyBootstrap.getPortOrDefault());
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("The native agent proxy fails to start, port{}",nativeAgentProxyBootstrap.getPortOrDefault());
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("shutdown native agent proxy");
        }

    }
