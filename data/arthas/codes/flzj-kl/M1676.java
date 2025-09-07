    public void handleWebSocketUpgrade(ChannelHandlerContext ctx, FullHttpRequest request) {
        URI uri = null;
        try {
            uri = new URI(request.uri());
        } catch (URISyntaxException e) {
            // 处理异常
            return;
        }

        Map<String, String> params = parseQueryString(uri.getQuery());

        String nativeAgentAddress = params.get("nativeAgentAddress");

        if (nativeAgentAddress != null) {
            ctx.channel().attr(AttributeKey.valueOf("nativeAgentAddress")).set(nativeAgentAddress);
        }

        request.setUri(uri.getPath());

        ctx.fireChannelRead(request.retain());
    }
