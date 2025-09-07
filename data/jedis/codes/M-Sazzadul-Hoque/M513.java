  @Override
  public long xack(byte[] key, byte[] group, byte[]... ids) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xack(key, group, ids));
  }
