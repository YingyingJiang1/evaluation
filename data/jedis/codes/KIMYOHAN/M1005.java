  public final CommandObject<Map<byte[], List<StreamEntryBinary>>> xreadBinaryAsMap(
      XReadParams xReadParams, Map.Entry<byte[], StreamEntryID>... streams) {
    CommandArguments args = commandArguments(XREAD).addParams(xReadParams).add(STREAMS);
    for (Map.Entry<byte[], StreamEntryID> entry : streams) {
      args.key(entry.getKey());
    }
    for (Map.Entry<byte[], StreamEntryID> entry : streams) {
      args.add(entry.getValue());
    }
    return new CommandObject<>(args, BuilderFactory.STREAM_READ_BINARY_MAP_RESPONSE);
  }
