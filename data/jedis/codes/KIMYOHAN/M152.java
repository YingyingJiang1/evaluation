  @Deprecated
  @Override
  public List<Object> xreadGroup(byte[] groupName, byte[] consumer,
      XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
    return executeCommand(
        commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
  }
