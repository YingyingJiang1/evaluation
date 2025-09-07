  @Override
  public List<Long> hpexpireAt(byte[] key, long unixTimeMillis, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireAt(key, unixTimeMillis, fields));
  }
