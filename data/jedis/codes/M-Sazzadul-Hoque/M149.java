  @Deprecated
  @Override
  public byte[] getSet(byte[] key, byte[] value) {
    return executeCommand(commandObjects.getSet(key, value));
  }
