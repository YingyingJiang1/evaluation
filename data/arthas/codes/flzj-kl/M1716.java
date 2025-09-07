                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH));
                            ch.pipeline().addLast(new HttpRequestHandler());
                        }
