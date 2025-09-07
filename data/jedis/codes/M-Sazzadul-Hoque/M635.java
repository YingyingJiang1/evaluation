  @Override
  public List<String> zdiff(String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiff(keys));
  }
