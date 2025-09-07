  @Override
  public List<Object> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrevrange(key, end, start, count));
  }
