  public final CommandObject<List<Map.Entry<String, List<StreamEntry>>>> xread(
      XReadParams xReadParams, Map<String, StreamEntryID> streams) {
    CommandArguments args = commandArguments(XREAD).addParams(xReadParams).add(STREAMS);
    Set<Map.Entry<String, StreamEntryID>> entrySet = streams.entrySet();
    entrySet.forEach(entry -> args.key(entry.getKey()));
    entrySet.forEach(entry -> args.add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.STREAM_READ_RESPONSE);
  }
