  public final CommandObject<Map<String, TSMRangeElements>> tsMRange(long fromTimestamp, long toTimestamp, String... filters) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.MRANGE).add(fromTimestamp)
        .add(toTimestamp).add(TimeSeriesKeyword.FILTER).addObjects((Object[]) filters),
        getTimeseriesMultiRangeResponseBuilder());
  }
