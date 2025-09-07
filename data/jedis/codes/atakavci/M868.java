  private void safeReAuthenticate(Token token) {
    try {
      byte[] rawPass = client.encodeToBytes(token.getValue().toCharArray());
      byte[] rawUser = client.encodeToBytes(token.getUser().toCharArray());

      Token newToken = pendingTokenRef.getAndSet(token);
      if (newToken == null) {
        commandSync.lock();
        try {
          sendAndFlushCommand(Command.AUTH, rawUser, rawPass);
          resultHandler.add(this.authResultHandler);
        } finally {
          pendingTokenRef.set(null);
          commandSync.unlock();
        }
      }
    } catch (Exception e) {
      logger.error("Error while re-authenticating connection", e);
      client.getAuthXManager().getListener().onConnectionAuthenticationError(e);
    }
  }
