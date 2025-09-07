    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!securityAuthenticator.needLogin()) {
            ctx.fireChannelRead(msg);
            return;
        }

        boolean authed = false;
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;

            // 判断session里是否有已登陆信息
            HttpSession session = httpSessionManager.getOrCreateHttpSession(ctx, httpRequest);
            if (session != null && session.getAttribute(ArthasConstants.SUBJECT_KEY) != null) {
                authed = true;
            }

            Principal principal = null;
            if (!authed) {
                // 判断请求header里是否带有 username/password
                principal = extractBasicAuthSubject(httpRequest);
                if (principal == null) {
                    // 判断 url里是否有 username/password
                    principal = extractBasicAuthSubjectFromUrl(httpRequest);
                }
            }
            if (!authed && principal == null) {
                // 判断是否本地连接
                principal = AuthUtils.localPrincipal(ctx);
            }
            Subject subject = securityAuthenticator.login(principal);
            if (subject != null) {
                authed = true;
                if (session != null) {
                    session.setAttribute(ArthasConstants.SUBJECT_KEY, subject);
                }
            }

            if (!authed) {
                // restricted resource, so send back 401 to require valid username/password
                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
                response.headers().set(HttpHeaderNames.WWW_AUTHENTICATE, "Basic realm=\"arthas webconsole\"");
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);

                ctx.writeAndFlush(response);
                // close the channel
                ctx.channel().close();
                return;
            }

        }

        ctx.fireChannelRead(msg);
    }
