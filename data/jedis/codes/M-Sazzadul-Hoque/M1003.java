  @Deprecated
  public final CommandObject<List<Object>> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
    CommandArguments args = commandArguments(XREAD).addParams(xReadParams).add(STREAMS);
    for (Map.Entry<byte[], byte[]> entry : streams) {
      args.key(entry.getKey());
    }
    for (Map.Entry<byte[], byte[]> entry : streams) {
      args.add(entry.getValue());
    }
    return new CommandObject<>(args, BuilderFactory.RAW_OBJECT_LIST);
  }
