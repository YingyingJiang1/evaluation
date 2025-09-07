    public static DefaultHttpResponse createResponse(FullHttpRequest request, HttpResponseStatus status, String content) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
        try {
            response.content().writeBytes(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return response;
    }
