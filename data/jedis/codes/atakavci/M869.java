  protected void processAuthReply(Object reply) {
    byte[] resp = (byte[]) reply;
    String response = SafeEncoder.encode(resp);
    if (!"OK".equals(response)) {
      String msg = "Re-authentication failed with server response: " + response;
      Exception failedAuth = new JedisAuthenticationException(msg);
      logger.error(failedAuth.getMessage(), failedAuth);
      client.getAuthXManager().getListener().onConnectionAuthenticationError(failedAuth);
    }
  }
