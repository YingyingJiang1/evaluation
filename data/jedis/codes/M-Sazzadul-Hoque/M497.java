  @Override
  public List<Long> hexpireTime(byte[] key, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireTime(key, fields));
  }
