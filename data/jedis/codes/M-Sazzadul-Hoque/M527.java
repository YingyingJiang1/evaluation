  @Override
  public boolean copy(String srcKey, String dstKey, int db, boolean replace) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.copy(srcKey, dstKey, db, replace));
  }
