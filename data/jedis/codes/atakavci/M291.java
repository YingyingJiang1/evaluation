  @Override
  public long hsetex(byte[] key, HSetExParams params, byte[] field, byte[] value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hsetex(key, params, field, value));
  }
