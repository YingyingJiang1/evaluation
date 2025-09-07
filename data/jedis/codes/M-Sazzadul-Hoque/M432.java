  @Override
  public String clientPause(final long timeout) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, PAUSE.getRaw(), toByteArray(timeout));
    return connection.getBulkReply();
  }
