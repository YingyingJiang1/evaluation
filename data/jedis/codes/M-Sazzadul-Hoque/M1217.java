  private ConnectionPool createNodePool(HostAndPort master) {
    if (masterPoolConfig == null) {
      if (clientSideCache == null) {
        return new ConnectionPool(master, masterClientConfig);
      } else {
        return new ConnectionPool(master, masterClientConfig, clientSideCache);
      }
    } else {
      if (clientSideCache == null) {
        return new ConnectionPool(master, masterClientConfig, masterPoolConfig);
      } else {
        return new ConnectionPool(master, masterClientConfig, clientSideCache, masterPoolConfig);
      }
    }
  }
