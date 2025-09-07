  @Override
  public List<String> zunion(ZParams params, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zunion(params, keys));
  }
