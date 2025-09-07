  @Override
  public long clientUnblock(final long clientId) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, UNBLOCK.getRaw(), toByteArray(clientId));
    return connection.getIntegerReply();
  }
