  @Override
  public String readonly() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(READONLY);
    return connection.getStatusCodeReply();
  }
