  @Override
  public String migrate(String host, int port, byte[] key, int timeout) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.migrate(host, port, key, timeout));
  }
