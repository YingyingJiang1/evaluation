  @Override
  public KeyValue<String, String> blpop(double timeout, String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blpop(timeout, key));
  }
