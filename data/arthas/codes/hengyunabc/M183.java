    public SimpleHttpResponse query(String targetUrl) throws InterruptedException {
        final Promise<SimpleHttpResponse> httpResponsePromise = GlobalEventExecutor.INSTANCE.newPromise();

        final EventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("arthas-ProxyClient", true));
        ChannelFuture closeFuture = null;
        try {
            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            b.group(group).channel(LocalChannel.class).handler(new ChannelInitializer<LocalChannel>() {
                @Override
                protected void initChannel(LocalChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new HttpClientCodec(), new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH),
                            new HttpProxyClientHandler(httpResponsePromise));
                }
            });

            LocalAddress localAddress = new LocalAddress(ArthasConstants.NETTY_LOCAL_ADDRESS);
            Channel localChannel = b.connect(localAddress).sync().channel();

            // Prepare the HTTP request.
            HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, targetUrl,
                    Unpooled.EMPTY_BUFFER);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);

            localChannel.writeAndFlush(request);

            closeFuture = localChannel.closeFuture();
            logger.info("proxy client connect to server success, targetUrl: " + targetUrl);

            return httpResponsePromise.get(5000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            logger.error("ProxyClient error, targetUrl: {}", targetUrl, e);
        } finally {
            if (closeFuture != null) {
                closeFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        group.shutdownGracefully();
                    }
                });
            } else {
                group.shutdownGracefully();
            }
        }

        SimpleHttpResponse httpResponse = new SimpleHttpResponse();
        try {
            httpResponse.setContent("error".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return httpResponse;
    }
