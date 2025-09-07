    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            // write cookie
            HttpResponse response = (HttpResponse) msg;
            Attribute<HttpSession> attribute = ctx.channel().attr(HttpSessionManager.SESSION_KEY);
            HttpSession session = attribute.get();
            if (session != null) {
                HttpSessionManager.setSessionCookie(response, session);
            }
        }
        super.write(ctx, msg, promise);
    }
