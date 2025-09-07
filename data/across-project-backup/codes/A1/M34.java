  @Override
  public boolean isCacheable(ProtocolCommand command, List<Object> keys) {
    if (allowCommands != null && !allowCommands.contains(command)) {
      return false;
    }
    if (denyCommands != null && denyCommands.contains(command)) {
      return false;
    }

    for (Object key : keys) {
      if (!(key instanceof String)) {
        return false;
      }
      if (allowKeys != null && !allowKeys.contains((String) key)) {
        return false;
      }
      if (denyKeys != null && denyKeys.contains((String) key)) {
        return false;
      }
    }

    return DefaultCacheable.isDefaultCacheableCommand(command);
  }
