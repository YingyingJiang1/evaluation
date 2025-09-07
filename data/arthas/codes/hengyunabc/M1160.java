    public HttpSession getOrCreateHttpSession(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        // 尝试用 ctx 和从 cookie里读取出 session
        Attribute<HttpSession> attribute = ctx.channel().attr(SESSION_KEY);
        HttpSession httpSession = attribute.get();
        if (httpSession != null) {
            return httpSession;
        }
        httpSession = getSession(httpRequest);
        if (httpSession != null) {
            attribute.set(httpSession);
            return httpSession;
        }
        // 创建session，并设置到ctx里
        httpSession = newHttpSession();
        attribute.set(httpSession);
        return httpSession;
    }
