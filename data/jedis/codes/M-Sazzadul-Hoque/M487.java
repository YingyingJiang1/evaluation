  @Override
  public List<Long> bitfieldReadonly(byte[] key, final byte[]... arguments) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitfieldReadonly(key, arguments));
  }
