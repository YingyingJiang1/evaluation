    protected static BasicPrincipal extractBasicAuthSubjectFromUrl(HttpRequest request) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> parameters = queryDecoder.parameters();

        List<String> passwords = parameters.get(ArthasConstants.PASSWORD_KEY);
        if (passwords == null || passwords.size() == 0) {
            return null;
        }
        String password = passwords.get(0);

        String username = ArthasConstants.DEFAULT_USERNAME;
        List<String> usernames = parameters.get(ArthasConstants.USERNAME_KEY);
        if (usernames != null && !usernames.isEmpty()) {
            username = usernames.get(0);
        }
        BasicPrincipal principal = new BasicPrincipal(username, password);
        logger.debug("Extracted Basic Auth principal from url: {}", principal);
        return principal;
    }
