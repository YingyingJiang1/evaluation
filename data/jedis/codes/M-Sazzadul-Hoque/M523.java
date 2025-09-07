  @Override
  public List<Object> xinfoGroups(byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xinfoGroups(key));
  }
