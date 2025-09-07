    private FullHttpResponse doListNativeAgent(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 1、Find native agent proxy address
        String address = httpNativeAgentProxyHandler.findAvailableProxyAddress();
        if (address == null || "".equals(address)) {
            return null;
        }
        // 2、Send Http request to native agent proxy to get native agent list
        String resStr = null;
        try {
            String url = "http://" + address + "/api/native-agent-proxy";
            String jsonBody = "{\"operation\":\"listNativeAgent\"}";
            resStr = OkHttpUtil.post(url, jsonBody);
        } catch (IOException e) {
            logger.error("Send http to native agent proxy failed");
            throw new RuntimeException(e);
        }
        if (resStr == null) {
            return null;
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                request.getProtocolVersion(),
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(resStr, StandardCharsets.UTF_8)
        );

        return response;
    }
