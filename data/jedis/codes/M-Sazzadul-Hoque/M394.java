  @Override
  public byte[] dump(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.dump(key));
  }
