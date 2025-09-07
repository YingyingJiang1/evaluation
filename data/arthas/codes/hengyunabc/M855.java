    @Override
    public void process(CommandProcess process) {
        int status = 0;
        String message = "";
        try {
            Session session = process.session();
            if (username == null) {
                status = 1;
                message = "username can not be empty!";
                return;
            }
            if (password == null) { // 没有传入password参数时，打印当前结果
                boolean authenticated = session.get(ArthasConstants.SUBJECT_KEY) != null;
                boolean needLogin = this.authenticator.needLogin();

                message = "Authentication result: " + authenticated + ", Need authentication: " + needLogin;
                if (needLogin && !authenticated) {
                    status = 1;
                }
                return;
            } else {
                // 尝试进行鉴权
                BasicPrincipal principal = new BasicPrincipal(username, password);
                try {
                    Subject subject = authenticator.login(principal);
                    if (subject != null) {
                        // 把subject 保存到 session里，后续其它命令则可以正常执行
                        session.put(ArthasConstants.SUBJECT_KEY, subject);
                        message = "Authentication result: " + true + ", username: " + username;
                    } else {
                        status = 1;
                        message = "Authentication result: " + false + ", username: " + username;
                    }
                } catch (LoginException e) {
                    logger.error("Authentication error, username: {}", username, e);
                }
            }
        } finally {
            process.end(status, message);
        }
    }
