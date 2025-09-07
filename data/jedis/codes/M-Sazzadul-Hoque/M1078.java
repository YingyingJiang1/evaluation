  private void helloAndAuth(final RedisProtocol protocol, final RedisCredentials credentials) {
    Map<String, Object> helloResult = null;
    if (protocol != null && credentials != null && credentials.getUser() != null) {
      byte[] rawPass = encodeToBytes(credentials.getPassword());
      try {
        helloResult = hello(encode(protocol.version()), Keyword.AUTH.getRaw(),
          encode(credentials.getUser()), rawPass);
      } finally {
        Arrays.fill(rawPass, (byte) 0); // clear sensitive data
      }
    } else {
      authenticate(credentials);
      helloResult = protocol == null ? null : hello(encode(protocol.version()));
    }
    if (helloResult != null) {
      server = (String) helloResult.get("server");
      version = (String) helloResult.get("version");
    }

    // clearing 'char[] credentials.getPassword()' should be
    // handled in RedisCredentialsProvider.cleanUp()
  }
