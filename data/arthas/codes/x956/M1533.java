    public static void updateCorsHeader(HttpHeaders headers) {
//        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS,
//                StringUtils.joinWith(",", "user-agent", "cache-control", "content-type", "content-transfer-encoding",
//                        "grpc-timeout", "keep-alive"));
        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");

        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.set(HttpHeaderNames.ACCESS_CONTROL_REQUEST_HEADERS, "content-type,x-grpc-web,x-user-agent");
        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,POST,HEAD");

//        headers.set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS,
//                StringUtils.joinWith(",", "grpc-status", "grpc-message"));
        headers.set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
    }
