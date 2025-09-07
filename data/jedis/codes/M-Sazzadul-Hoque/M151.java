  @Override
  @Deprecated
  public long zdiffStore(byte[] dstkey, byte[]... keys) {
    return executeCommand(commandObjects.zdiffStore(dstkey, keys));
  }
