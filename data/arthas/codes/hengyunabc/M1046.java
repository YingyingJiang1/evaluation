    private void checkPermission(Session session, CliToken token) {
        if (ArthasBootstrap.getInstance().getSecurityAuthenticator().needLogin()) {
            // 检查session是否有 Subject
            Object subject = session.get(ArthasConstants.SUBJECT_KEY);
            if (subject == null) {
                if (token != null && token.isText() && token.value().trim().equals(ArthasConstants.AUTH)) {
                    // 执行的是auth 命令
                    return;
                }
                throw new IllegalArgumentException("Error! command not permitted, try to use 'auth' command to authenticates.");
            }
        }
    }
