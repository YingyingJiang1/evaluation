  @Override
  public List<Long> hpexpire(byte[] key, long milliseconds, ExpiryOption condition, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpire(key, milliseconds, condition, fields));
  }
