  @Deprecated
  @Override
  public Response<List<Object>> xreadGroup(byte[] groupName, byte[] consumer,
      XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
    return appendCommand(commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
  }
