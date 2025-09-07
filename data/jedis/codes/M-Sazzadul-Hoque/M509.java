  @Override
  public List<Object> xrange(byte[] key, byte[] start, byte[] end) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrange(key, start, end));
  }
