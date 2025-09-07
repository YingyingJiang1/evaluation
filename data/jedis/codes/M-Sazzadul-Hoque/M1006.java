  @Deprecated
  public final CommandObject<List<Object>> xreadGroup(byte[] groupName, byte[] consumer,
      XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
    CommandArguments args = commandArguments(XREADGROUP)
        .add(GROUP).add(groupName).add(consumer)
        .addParams(xReadGroupParams).add(STREAMS);
    for (Map.Entry<byte[], byte[]> entry : streams) {
      args.key(entry.getKey());
    }
    for (Map.Entry<byte[], byte[]> entry : streams) {
      args.add(entry.getValue());
    }
    return new CommandObject<>(args, BuilderFactory.RAW_OBJECT_LIST);
  }
