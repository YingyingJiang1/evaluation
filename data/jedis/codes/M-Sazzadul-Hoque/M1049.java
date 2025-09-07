  public final CommandObject<Map<String, TSMRangeElements>> tsMRevRange(long fromTimestamp, long toTimestamp, String... filters) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.MREVRANGE).add(fromTimestamp)
        .add(toTimestamp).add(TimeSeriesKeyword.FILTER).addObjects((Object[]) filters),
        getTimeseriesMultiRangeResponseBuilder());
  }
