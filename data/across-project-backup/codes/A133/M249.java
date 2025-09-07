    private void agentRegister(ChannelHandlerContext ctx, HandshakeComplete handshake, String requestUri) throws URISyntaxException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(requestUri);
        Map<String, List<String>> parameters = queryDecoder.parameters();

        String appName = null;
        List<String> appNameList = parameters.get(URIConstans.APP_NAME);
        if (appNameList != null && !appNameList.isEmpty()) {
            appName = appNameList.get(0);
        }

        // generate a random agent id
        String id = null;
        if (appName != null) {
            // 如果有传 app name，则生成带 app name前缀的id，方便管理
            id = appName + "_" + RandomStringUtils.random(20, true, true).toUpperCase();
        } else {
            id = RandomStringUtils.random(20, true, true).toUpperCase();
        }
        // agent传过来，则优先用 agent的
        List<String> idList = parameters.get(URIConstans.ID);
        if (idList != null && !idList.isEmpty()) {
            id = idList.get(0);
        }

        String arthasVersion = null;
        List<String> arthasVersionList = parameters.get(URIConstans.ARTHAS_VERSION);
        if (arthasVersionList != null && !arthasVersionList.isEmpty()) {
            arthasVersion = arthasVersionList.get(0);
        }

        final String finalId = id;

        // URI responseUri = new URI("response", null, "/", "method=" + MethodConstants.AGENT_REGISTER + "&id=" + id, null);
        URI responseUri = UriComponentsBuilder.newInstance().scheme(URIConstans.RESPONSE).path("/")
                .queryParam(URIConstans.METHOD, MethodConstants.AGENT_REGISTER).queryParam(URIConstans.ID, id).build()
                .encode().toUri();

        AgentInfo info = new AgentInfo();

        // 前面可能有nginx代理
        HttpHeaders headers = handshake.requestHeaders();
        String host = HttpUtils.findClientIP(headers);

        if (host == null) {
            SocketAddress remoteAddress = ctx.channel().remoteAddress();
            if (remoteAddress instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
                info.setHost(inetSocketAddress.getHostString());
                info.setPort(inetSocketAddress.getPort());
            }
        } else {
            info.setHost(host);
            Integer port = HttpUtils.findClientPort(headers);
            if (port != null) {
                info.setPort(port);
            }
        }

        info.setChannelHandlerContext(ctx);
        if (arthasVersion != null) {
            info.setArthasVersion(arthasVersion);
        }

        tunnelServer.addAgent(id, info);
        ctx.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                tunnelServer.removeAgent(finalId);
            }

        });

        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseUri.toString()));
    }
