  private static CommandObjects createCommandObjects(Connection connection) {
    CommandObjects commandObjects = new CommandObjects();
    RedisProtocol proto = connection.getRedisProtocol();
    if (proto != null) commandObjects.setProtocol(proto);
    return commandObjects;
  }
