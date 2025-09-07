  @Override
  public KeyValue<String, String> brpop(double timeout, String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.brpop(timeout, key));
  }
