  private void checkConnectionSuitableForPubSub() {
    if (authenticator.client.protocol != RedisProtocol.RESP3
        && authenticator.client.isTokenBasedAuthenticationEnabled()) {
      throw new JedisException(
          "Blocking pub/sub operations are not supported on token-based authentication enabled connections with RESP2 protocol!");
    }
  }
