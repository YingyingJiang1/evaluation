  private ConnectionPool createNodePool(HostAndPort node) {
    if (poolConfig == null) {
      if (clientSideCache == null) {
        return new ConnectionPool(node, clientConfig);
      } else {
        return new ConnectionPool(node, clientConfig, clientSideCache);
      }
    } else {
      if (clientSideCache == null) {
        return new ConnectionPool(node, clientConfig, poolConfig);
      } else {
        return new ConnectionPool(node, clientConfig, clientSideCache, poolConfig);
      }
    }
  }
