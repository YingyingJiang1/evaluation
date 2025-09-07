  @Override
  public String clientKill(final byte[] ipPort) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, KILL.getRaw(), ipPort);
    return this.connection.getStatusCodeReply();
  }
