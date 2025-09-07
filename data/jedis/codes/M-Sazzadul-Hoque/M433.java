  @Override
  public String clientPause(final long timeout, final ClientPauseMode mode) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, PAUSE.getRaw(), toByteArray(timeout), mode.getRaw());
    return connection.getBulkReply();
  }
