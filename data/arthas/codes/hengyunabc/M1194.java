    public void start(final Supplier<TelnetHandler> handlerFactory, final Consumer<TtyConnection> factory,
                    final Consumer<Throwable> doneHandler) {
        ServerBootstrap boostrap = new ServerBootstrap();
        boostrap.group(group).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ProtocolDetectHandler(channelGroup, handlerFactory, factory, workerGroup, httpSessionManager));
                            }
                        });

        boostrap.bind(getHost(), getPort()).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    doneHandler.accept(null);
                } else {
                    doneHandler.accept(future.cause());
                }
            }
        });
    }
