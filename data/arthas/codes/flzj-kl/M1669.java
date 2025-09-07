    private FullHttpResponse doListNativeAgent(ChannelHandlerContext ctx, FullHttpRequest request) {
        NativeAgentDiscoveryFactory nativeAgentDiscoveryFactory = NativeAgentDiscoveryFactory.getNativeAgentDiscoveryFactory();
        NativeAgentDiscovery nativeAgentDiscovery = nativeAgentDiscoveryFactory.getNativeAgentDiscovery(NativeAgentProxyBootstrap.agentRegistrationType);
        Map<String, String> nativeAgentMap = nativeAgentDiscovery.findNativeAgent(NativeAgentProxyBootstrap.agentRegistrationAddress);

        List<NativeAgentInfoDTO> nativeAgentInfoList = new ArrayList<>();

        for (Map.Entry<String, String> entry : nativeAgentMap.entrySet()) {
            String nativeAgentIp = entry.getKey();
            String value = entry.getValue();
            String[] split = value.split(":");
            nativeAgentInfoList.add(new NativeAgentInfoDTO(nativeAgentIp, Integer.valueOf(split[0]), Integer.valueOf(split[1])));
        }

        String nativeAgentInfoStr = JSON.toJSONString(nativeAgentInfoList);

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                request.getProtocolVersion(),
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(nativeAgentInfoStr, StandardCharsets.UTF_8)
        );

        // 设置跨域响应头
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type, Authorization");

        // 设置其他必要的头部
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        return response;
    }
