  @Override
  public boolean copy(String srcKey, String dstKey, boolean replace) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.copy(srcKey, dstKey, replace));
  }
