  @Override
  public String functionKill() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionKill());
  }
