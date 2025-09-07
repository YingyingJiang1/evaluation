  protected void registerForAuthentication(Connection newClient) {
    Connection oldClient = this.client;
    if (oldClient == newClient) return;
    if (oldClient != null && oldClient.getAuthXManager() != null) {
      oldClient.getAuthXManager().removePostAuthenticationHook(authenticationHandler);
    }
    if (newClient != null && newClient.getAuthXManager() != null) {
      newClient.getAuthXManager().addPostAuthenticationHook(authenticationHandler);
    }
    this.client = newClient;
  }
