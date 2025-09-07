    public FullHttpResponse responseFindAvailableProxyAddress(ChannelHandlerContext ctx, FullHttpRequest request) {
        String availableProxyAddress = findAvailableProxyAddress();
        if (availableProxyAddress == null || "".equals(availableProxyAddress)) {
            return null;
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                request.getProtocolVersion(),
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(availableProxyAddress, StandardCharsets.UTF_8)
        );
        return response;
    }
