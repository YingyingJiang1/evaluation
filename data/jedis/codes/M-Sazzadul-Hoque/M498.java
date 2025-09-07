  @Override
  public List<Long> hpexpireTime(byte[] key, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireTime(key, fields));
  }
