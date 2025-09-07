  public final CommandObject<List<Long>> tsMAdd(Map.Entry<String, TSElement>... entries) {
    CommandArguments args = commandArguments(TimeSeriesCommand.MADD);
    for (Map.Entry<String, TSElement> entry : entries) {
      args.key(entry.getKey()).add(entry.getValue().getTimestamp()).add(entry.getValue().getValue());
    }
    return new CommandObject<>(args, BuilderFactory.LONG_LIST);
  }
