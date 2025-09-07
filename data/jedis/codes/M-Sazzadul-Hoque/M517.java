  @Override
  public long xdel(byte[] key, byte[]... ids) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xdel(key, ids));
  }
