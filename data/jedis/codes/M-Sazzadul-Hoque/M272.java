  @Override
  public String flushAll() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.flushAll());
  }
