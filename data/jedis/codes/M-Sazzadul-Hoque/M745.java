  @Override
  public byte[] dump(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.dump(key));
  }
