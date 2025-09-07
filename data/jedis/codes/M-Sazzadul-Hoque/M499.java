  @Override
  public List<Long> httl(byte[] key, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.httl(key, fields));
  }
