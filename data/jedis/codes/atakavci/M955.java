    private void tokenManagerStart() {
        tokenManager.start(new TokenListener() {
            @Override
            public void onTokenRenewed(Token token) {
                currentToken = token;
                authenticateConnections(token);
            }

            @Override
            public void onError(Exception reason) {
                listener.onIdentityProviderError(reason);
            }
        }, true);
    }
