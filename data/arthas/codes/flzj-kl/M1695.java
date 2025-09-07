    private FullHttpResponse doAttachJvm(ChannelHandlerContext ctx, FullHttpRequest request, Integer pid) {
        String httpPort = "";
        try {
            httpPort = JvmAttachmentHandler.attachJvmByPid(pid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String attachSuccessMsg = httpPort;

        DefaultFullHttpResponse response = buildHttpCorsResponse(attachSuccessMsg);
        return response;
    }
