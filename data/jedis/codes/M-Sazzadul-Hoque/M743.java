  @Override
  public String sentinelRemove(String masterName) {
    connection.sendCommand(SENTINEL, REMOVE.name(), masterName);
    return connection.getStatusCodeReply();
  }
