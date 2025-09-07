  @Override
  public String rpop(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rpop(key));
  }
