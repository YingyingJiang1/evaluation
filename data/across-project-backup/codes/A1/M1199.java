  private void initializeSlotsCache(Set<HostAndPort> startNodes, JedisClientConfig clientConfig) {
    if (startNodes.isEmpty()) {
      throw new JedisClusterOperationException("No nodes to initialize cluster slots cache.");
    }

    ArrayList<HostAndPort> startNodeList = new ArrayList<>(startNodes);
    Collections.shuffle(startNodeList);

    JedisException firstException = null;
    for (HostAndPort hostAndPort : startNodeList) {
      try (Connection jedis = new Connection(hostAndPort, clientConfig)) {
        cache.discoverClusterNodesAndSlots(jedis);
        return;
      } catch (JedisException e) {
        if (firstException == null) {
          firstException = e;
        }
        // try next nodes
      }
    }

    if (System.getProperty(INIT_NO_ERROR_PROPERTY) != null) {
      return;
    }
    JedisClusterOperationException uninitializedException
        = new JedisClusterOperationException("Could not initialize cluster slots cache.");
    uninitializedException.addSuppressed(firstException);
    throw uninitializedException;
  }
