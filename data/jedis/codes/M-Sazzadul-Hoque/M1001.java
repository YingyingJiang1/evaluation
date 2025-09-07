  public final CommandObject<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(
      String groupName, String consumer, XReadGroupParams xReadGroupParams,
      Map<String, StreamEntryID> streams) {
    CommandArguments args = commandArguments(XREADGROUP)
        .add(GROUP).add(groupName).add(consumer)
        .addParams(xReadGroupParams).add(STREAMS);
    Set<Map.Entry<String, StreamEntryID>> entrySet = streams.entrySet();
    entrySet.forEach(entry -> args.key(entry.getKey()));
    entrySet.forEach(entry -> args.add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.STREAM_READ_RESPONSE);
  }
