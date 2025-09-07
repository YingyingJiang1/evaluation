    public static HttpResponse createRedirectResponse(FullHttpRequest request, String url) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, url);
        return response;
    }
