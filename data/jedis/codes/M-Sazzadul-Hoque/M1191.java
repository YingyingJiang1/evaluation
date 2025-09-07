  public static RedisProtocol getRedisProtocol(URI uri) {
    if (uri.getQuery() == null) return null;

    String[] params = uri.getQuery().split("&");
    for (String param : params) {
      int idx = param.indexOf("=");
      if (idx < 0) continue;
      if ("protocol".equals(param.substring(0, idx))) {
        String ver = param.substring(idx + 1);
        for (RedisProtocol proto : RedisProtocol.values()) {
          if (proto.version().equals(ver)) {
            return proto;
          }
        }
        throw new IllegalArgumentException("Unknown protocol " + ver);
      }
    }
    return null; // null (default) when not defined
  }
