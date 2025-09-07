  @Override
  public long persist(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.persist(key));
  }
