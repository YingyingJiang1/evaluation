  @Override
  public List<Object> xinfoConsumers(byte[] key, byte[] group) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xinfoConsumers(key, group));
  }
