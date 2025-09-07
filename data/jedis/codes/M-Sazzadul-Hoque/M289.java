  @Override
  public long move(final byte[] key, final int dbIndex) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MOVE, key, toByteArray(dbIndex));
    return connection.getIntegerReply();
  }
