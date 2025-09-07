    protected static BasicPrincipal extractBasicAuthSubject(HttpRequest request) {
        String auth = request.headers().get(HttpHeaderNames.AUTHORIZATION);
        if (auth != null) {
            String constraint = StringUtils.before(auth, " ");
            if (constraint != null) {
                if ("Basic".equalsIgnoreCase(constraint.trim())) {
                    String decoded = StringUtils.after(auth, " ");
                    if (decoded == null) {
                        logger.error("Extracted Basic Auth principal failed, bad auth String: {}", auth);
                        return null;
                    }
                    // the decoded part is base64 encoded, so we need to decode that
                    ByteBuf buf = Unpooled.wrappedBuffer(decoded.getBytes());
                    ByteBuf out = Base64.decode(buf);
                    String userAndPw = out.toString(Charset.defaultCharset());
                    String username = StringUtils.before(userAndPw, ":");
                    String password = StringUtils.after(userAndPw, ":");
                    BasicPrincipal principal = new BasicPrincipal(username, password);
                    logger.debug("Extracted Basic Auth principal from HTTP header: {}", principal);
                    return principal;
                }
            }
        }
        return null;
    }
