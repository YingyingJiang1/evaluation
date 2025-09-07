    public static void main(String[] args) {
        // Print welcome message
        WelcomeUtil.printManagementWebWelcomeMsg();

        // Startup parameter analysis
        logger.info("read input config...");
        NativeAgentManagementWebBootstrap nativeAgentManagementWebBootstrap = new NativeAgentManagementWebBootstrap();
        CLI cli = CLIConfigurator.define(NativeAgentManagementWebBootstrap.class);
        CommandLine commandLine = cli.parse(Arrays.asList(args));
        try {
            CLIConfigurator.inject(commandLine, nativeAgentManagementWebBootstrap);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("read input success!");

        // Start the http server
        logger.info("start the http server... httPort:{}", nativeAgentManagementWebBootstrap.getPortOrDefault());
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
                            ch.pipeline().addLast(new HttpRequestHandler());
                        }
                    });
            ChannelFuture f = b.bind(nativeAgentManagementWebBootstrap.getPortOrDefault()).sync();
            logger.info("start the http server success! htt port:{}", nativeAgentManagementWebBootstrap.getPortOrDefault());
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("The native agent server fails to start, http port{}", nativeAgentManagementWebBootstrap.getPortOrDefault());
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("shutdown native agent server");
        }
    }
