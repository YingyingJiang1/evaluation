    public FullHttpResponse handlerResources(FullHttpRequest request, String path) {
        try {
            if (request == null || path == null) {
                return null;
            }
            String normalizedPath = normalizePath(path);
            if (normalizedPath == null) {
                return null;
            }
            URL resourceUrl = getClass().getResource(RESOURCES_BASE_PATH + normalizedPath);
            if (resourceUrl == null) {
                return null;
            }
            try (InputStream is = resourceUrl.openStream()) {
                if (is == null) {
                    return null;
                }

                ByteBuf content = readInputStream(is);
                FullHttpResponse response = new DefaultFullHttpResponse(
                        request.protocolVersion(), HttpResponseStatus.OK, content);

                HttpHeaders headers = response.headers();
                headers.set(HttpHeaderNames.CONTENT_TYPE, getContentType(normalizedPath));
                headers.setInt(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

                return response;
            }
        } catch (Exception e) {
            logger.error("");
            return null;
        }
    }
