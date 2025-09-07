  public final CommandObject<List<Map.Entry<byte[], List<StreamEntryBinary>>>> xreadGroupBinary(
      byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams, 
      Map.Entry<byte[], StreamEntryID>... streams) {
    CommandArguments args = commandArguments(XREADGROUP)
        .add(GROUP).add(groupName).add(consumer)
        .addParams(xReadGroupParams).add(STREAMS);
    for (Map.Entry<byte[], StreamEntryID> entry : streams) {
      args.key(entry.getKey());
    }
    for (Map.Entry<byte[], StreamEntryID> entry : streams) {
      args.add(entry.getValue());
    }
    return new CommandObject<>(args, BuilderFactory.STREAM_READ_BINARY_RESPONSE);
  }
