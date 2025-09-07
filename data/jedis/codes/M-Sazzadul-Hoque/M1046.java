  @Deprecated
  public final CommandObject<Long> tsAdd(String key, long timestamp, double value, TSCreateParams createParams) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.ADD).key(key).add(timestamp).add(value)
        .addParams(createParams), BuilderFactory.LONG);
  }
