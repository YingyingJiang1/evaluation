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
