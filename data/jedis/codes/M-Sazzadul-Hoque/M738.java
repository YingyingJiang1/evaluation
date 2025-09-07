  @Override
  public Long sentinelReset(String pattern) {
    connection.sendCommand(SENTINEL, SentinelKeyword.RESET.name(), pattern);
    return connection.getIntegerReply();
  }
