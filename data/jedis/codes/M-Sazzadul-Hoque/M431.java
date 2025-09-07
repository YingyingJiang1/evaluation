  @Override
  public long clientUnblock(final long clientId, final UnblockType unblockType) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, UNBLOCK.getRaw(), toByteArray(clientId), unblockType.getRaw());
    return connection.getIntegerReply();
  }
