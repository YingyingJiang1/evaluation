    private HttpSession getSession(HttpRequest httpRequest) {
        // TODO 增加从 url中获取 session id 功能？

        Set<Cookie> cookies;
        String value = httpRequest.headers().get(HttpHeaderNames.COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.STRICT.decode(value);
        }
        for (Cookie cookie : cookies) {
            if (ArthasConstants.ASESSION_KEY.equals(cookie.name())) {
                String sessionId = cookie.value();
                return sessions.get(sessionId);
            }
        }
        return null;
    }
