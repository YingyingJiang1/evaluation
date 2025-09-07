  @Override
  public List<Long> hpexpire(byte[] key, long milliseconds, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpire(key, milliseconds, fields));
  }
