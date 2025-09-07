  @Override
  public String flushDB() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.flushDB());
  }
