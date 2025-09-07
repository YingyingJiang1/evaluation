  private ConnectionPool setupNodeIfNotExist(final HostAndPort node) {
    String nodeKey = node.toString();
    ConnectionPool existingPool = resources.get(nodeKey);
    if (existingPool != null) return existingPool;

    ConnectionPool nodePool = poolConfig == null ? new ConnectionPool(node, clientConfig)
        : new ConnectionPool(node, clientConfig, poolConfig);
    resources.put(nodeKey, nodePool);
    return nodePool;
  }
