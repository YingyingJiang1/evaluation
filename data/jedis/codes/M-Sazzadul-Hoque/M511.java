  @Override
  public List<Object> xrevrange(byte[] key, byte[] end, byte[] start) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrevrange(key, end, start));
  }
