  @Override
  public List<Long> hexpire(byte[] key, long seconds, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpire(key, seconds, fields));
  }
