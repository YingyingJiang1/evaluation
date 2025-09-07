  @Override
  public List<Long> hpexpireAt(byte[] key, long unixTimeMillis, ExpiryOption condition, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireAt(key, unixTimeMillis, condition, fields));
  }
