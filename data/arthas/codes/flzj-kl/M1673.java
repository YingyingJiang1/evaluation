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
