  private static ClusterCommandObjects createClusterCommandObjects(RedisProtocol protocol) {
    ClusterCommandObjects cco = new ClusterCommandObjects();
    if (protocol == RedisProtocol.RESP3) cco.setProtocol(protocol);
    return cco;
  }
