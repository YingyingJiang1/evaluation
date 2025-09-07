  public final CommandObject<List<Map.Entry<byte[], List<StreamEntryBinary>>>> xreadGroupBinary(
          byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams,
          Map<byte[], StreamEntryID> streams) {
      CommandArguments args = commandArguments(XREADGROUP)
              .add(GROUP).add(groupName).add(consumer)
              .addParams(xReadGroupParams).add(STREAMS);
      Set<Map.Entry<byte[], StreamEntryID>> entrySet = streams.entrySet();
      entrySet.forEach(entry -> args.key(entry.getKey()));
      entrySet.forEach(entry -> args.add(entry.getValue()));
      return new CommandObject<>(args, BuilderFactory.STREAM_READ_BINARY_RESPONSE);
  }
