  @Override
  public List<Long> hpersist(byte[] key, byte[]... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpersist(key, fields));
  }
