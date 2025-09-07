  @Override
  public List<Long> hexpireAt(byte[] key, long unixTimeSeconds, ExpiryOption condition, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireAt(key, unixTimeSeconds, condition, fields));
  }
