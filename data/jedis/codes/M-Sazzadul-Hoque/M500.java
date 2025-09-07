  @Override
  public List<Long> hpttl(byte[] key, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpttl(key, fields));
  }
