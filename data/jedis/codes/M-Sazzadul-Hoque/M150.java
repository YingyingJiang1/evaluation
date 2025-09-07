  @Override
  @Deprecated
  public long zdiffStore(String dstkey, String... keys) {
    return executeCommand(commandObjects.zdiffStore(dstkey, keys));
  }
