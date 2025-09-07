  @Override
  public List<Long> hexpire(byte[] key, long seconds, ExpiryOption condition, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpire(key, seconds, condition, fields));
  }
