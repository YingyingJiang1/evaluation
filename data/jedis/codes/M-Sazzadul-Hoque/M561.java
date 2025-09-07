  @Override
  public long move(final String key, final int dbIndex) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MOVE, encode(key), toByteArray(dbIndex));
    return connection.getIntegerReply();
  }
