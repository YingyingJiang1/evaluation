  public final CommandObject<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType,
      long timeBucket) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.CREATERULE).key(sourceKey).key(destKey)
        .add(TimeSeriesKeyword.AGGREGATION).add(aggregationType).add(timeBucket), BuilderFactory.STRING);
  }
