  public final CommandObject<Map<String, List<StreamEntry>>> xreadAsMap(
      XReadParams xReadParams, Map<String, StreamEntryID> streams) {
    CommandArguments args = commandArguments(XREAD).addParams(xReadParams).add(STREAMS);
    Set<Map.Entry<String, StreamEntryID>> entrySet = streams.entrySet();
    entrySet.forEach(entry -> args.key(entry.getKey()));
    entrySet.forEach(entry -> args.add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.STREAM_READ_MAP_RESPONSE);
  }
