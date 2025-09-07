  @Override
  public List<Long> hexpireAt(byte[] key, long unixTimeSeconds, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireAt(key, unixTimeSeconds, fields));
  }
