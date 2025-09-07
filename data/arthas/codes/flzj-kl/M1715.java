                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    ChannelPipeline p = ch.pipeline();
                                    p.addLast(new HttpRequestDecoder());
                                    p.addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH));
                                    p.addLast(new HttpResponseEncoder());
                                    p.addLast(new WebSocketServerProtocolHandler("/ws"));
                                    p.addLast(new ForwardClientSocketClientHandler());
                                }
