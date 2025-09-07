    public void authenticateConnections(Token token) {
        RedisCredentials credentialsFromToken = new TokenCredentials(token);
        for (WeakReference<Connection> connectionRef : connections) {
            Connection connection = connectionRef.get();
            if (connection != null) {
                connection.setCredentials(credentialsFromToken);
            } else {
                connections.remove(connectionRef);
            }
        }
        postAuthenticateHooks.forEach(hook -> hook.accept(token));
    }
