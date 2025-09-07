    public ChannelFuture connect(boolean reconnect) throws SSLException, URISyntaxException, InterruptedException {
        QueryStringEncoder queryEncoder = new QueryStringEncoder(this.tunnelServerUrl);
        queryEncoder.addParam(URIConstans.METHOD, MethodConstants.AGENT_REGISTER);
        queryEncoder.addParam(URIConstans.ARTHAS_VERSION, this.version);
        if (appName != null) {
            queryEncoder.addParam(URIConstans.APP_NAME, appName);
        }
        if (id != null) {
            queryEncoder.addParam(URIConstans.ID, id);
        }
        // ws://127.0.0.1:7777/ws?method=agentRegister
        final URI agentRegisterURI = queryEncoder.toUri();

        logger.info("Try to register arthas agent, uri: {}", agentRegisterURI);

        String scheme = agentRegisterURI.getScheme() == null ? "ws" : agentRegisterURI.getScheme();
        final String host = agentRegisterURI.getHost() == null ? "127.0.0.1" : agentRegisterURI.getHost();
        final int port;
        if (agentRegisterURI.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = agentRegisterURI.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only WS(S) is supported. tunnelServerUrl: " + tunnelServerUrl);
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        WebSocketClientProtocolConfig clientProtocolConfig = WebSocketClientProtocolConfig.newBuilder()
                .webSocketUri(agentRegisterURI)
                .maxFramePayloadLength(ArthasConstants.MAX_HTTP_CONTENT_LENGTH).build();

        final WebSocketClientProtocolHandler websocketClientHandler = new WebSocketClientProtocolHandler(
                clientProtocolConfig);
        final TunnelClientSocketClientHandler handler = new TunnelClientSocketClientHandler(TunnelClient.this);

        Bootstrap bs = new Bootstrap();

        bs.group(eventLoopGroup)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .option(ChannelOption.TCP_NODELAY, true)
        .channel(NioSocketChannel.class).remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                        }

                        p.addLast(new HttpClientCodec(), new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH), websocketClientHandler,
                                new IdleStateHandler(0, 0, ArthasConstants.WEBSOCKET_IDLE_SECONDS),
                                handler);
                    }
                });

        ChannelFuture connectFuture = bs.connect();
        if (reconnect) {
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.cause() != null) {
                        logger.error("connect to tunnel server error, uri: {}", tunnelServerUrl, future.cause());
                    }
                }
            });
        }
        connectFuture.sync();

        return handler.registerFuture();
    }
