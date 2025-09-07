  public final CommandObject<Map<byte[], List<StreamEntryBinary>>> xreadBinaryAsMap(
          XReadParams xReadParams, Map<byte[], StreamEntryID> streams) {
      CommandArguments args = commandArguments(XREAD).addParams(xReadParams).add(STREAMS);
      Set<Map.Entry<byte[], StreamEntryID>> entrySet = streams.entrySet();
      entrySet.forEach(entry -> args.key(entry.getKey()));
      entrySet.forEach(entry -> args.add(entry.getValue()));
      return new CommandObject<>(args, BuilderFactory.STREAM_READ_BINARY_MAP_RESPONSE);
  }
