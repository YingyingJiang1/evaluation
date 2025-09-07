  @Override
  public String readwrite() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(READWRITE);
    return connection.getStatusCodeReply();
  }
