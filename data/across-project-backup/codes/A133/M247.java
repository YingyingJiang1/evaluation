    private void connectArthas(ChannelHandlerContext tunnelSocketCtx, MultiValueMap<String, String> parameters)
            throws URISyntaxException {

        List<String> agentId = parameters.getOrDefault("id", Collections.emptyList());

        if (agentId.isEmpty()) {
            logger.error("arthas agent id can not be null, parameters: {}", parameters);
            throw new IllegalArgumentException("arthas agent id can not be null");
        }

        logger.info("try to connect to arthas agent, id: " + agentId.get(0));

        Optional<AgentInfo> findAgent = tunnelServer.findAgent(agentId.get(0));

        if (findAgent.isPresent()) {
            ChannelHandlerContext agentCtx = findAgent.get().getChannelHandlerContext();

            String clientConnectionId = RandomStringUtils.random(20, true, true).toUpperCase();

            logger.info("random clientConnectionId: " + clientConnectionId);
            // URI uri = new URI("response", null, "/",
            //        "method=" + MethodConstants.START_TUNNEL + "&id=" + agentId.get(0) + "&clientConnectionId=" + clientConnectionId, null);
            URI uri = UriComponentsBuilder.newInstance().scheme(URIConstans.RESPONSE).path("/")
                    .queryParam(URIConstans.METHOD, MethodConstants.START_TUNNEL).queryParam(URIConstans.ID, agentId)
                    .queryParam(URIConstans.CLIENT_CONNECTION_ID, clientConnectionId).build().toUri();

            logger.info("startTunnel response: " + uri);

            ClientConnectionInfo clientConnectionInfo = new ClientConnectionInfo();
            SocketAddress remoteAddress = tunnelSocketCtx.channel().remoteAddress();
            if (remoteAddress instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
                clientConnectionInfo.setHost(inetSocketAddress.getHostString());
                clientConnectionInfo.setPort(inetSocketAddress.getPort());
            }
            clientConnectionInfo.setChannelHandlerContext(tunnelSocketCtx);

            // when the agent open tunnel success, will set result into the promise
            Promise<Channel> promise = GlobalEventExecutor.INSTANCE.newPromise();
            promise.addListener(new FutureListener<Channel>() {
                @Override
                public void operationComplete(final Future<Channel> future) throws Exception {
                    final Channel outboundChannel = future.getNow();
                    if (future.isSuccess()) {
                        tunnelSocketCtx.pipeline().remove(TunnelSocketFrameHandler.this);

                        // outboundChannel is form arthas agent
                        outboundChannel.pipeline().removeLast();

                        outboundChannel.pipeline().addLast(new RelayHandler(tunnelSocketCtx.channel()));
                        tunnelSocketCtx.pipeline().addLast(new RelayHandler(outboundChannel));
                    } else {
                        logger.error("wait for agent connect error. agentId: {}, clientConnectionId: {}", agentId,
                                clientConnectionId);
                        ChannelUtils.closeOnFlush(agentCtx.channel());
                    }
                }
            });

            clientConnectionInfo.setPromise(promise);
            this.tunnelServer.addClientConnectionInfo(clientConnectionId, clientConnectionInfo);
            tunnelSocketCtx.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    tunnelServer.removeClientConnectionInfo(clientConnectionId);
                }
            });

            agentCtx.channel().writeAndFlush(new TextWebSocketFrame(uri.toString()));

            logger.info("browser connect waitting for arthas agent open tunnel");
            boolean watiResult = promise.awaitUninterruptibly(20, TimeUnit.SECONDS);
            if (watiResult) {
                logger.info(
                        "browser connect wait for arthas agent open tunnel success, agentId: {}, clientConnectionId: {}",
                        agentId, clientConnectionId);
            } else {
                logger.error(
                        "browser connect wait for arthas agent open tunnel timeout, agentId: {}, clientConnectionId: {}",
                        agentId, clientConnectionId);
                tunnelSocketCtx.close();
            }
        } else {
            tunnelSocketCtx.channel().writeAndFlush(new CloseWebSocketFrame(2000, "Can not find arthas agent by id: "+ agentId));
            logger.error("Can not find arthas agent by id: {}", agentId);
            throw new IllegalArgumentException("Can not find arthas agent by id: " + agentId);
        }
    }
