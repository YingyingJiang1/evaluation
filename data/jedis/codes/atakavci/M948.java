  private void attachAuthenticationListener(AuthXManager authXManager) {
    this.authXManager = authXManager;
    if (authXManager != null) {
      authXManager.addPostAuthenticationHook(token -> {
        try {
          // this is to trigger validations on each connection via ConnectionFactory
          evict();
        } catch (Exception e) {
          throw new JedisException("Failed to evict connections from pool", e);
        }
      });
    }
  }
