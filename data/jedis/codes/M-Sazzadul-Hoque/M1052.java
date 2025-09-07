  public final CommandObject<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType,
      long bucketDuration, long alignTimestamp) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.CREATERULE).key(sourceKey).key(destKey)
        .add(TimeSeriesKeyword.AGGREGATION).add(aggregationType).add(bucketDuration).add(alignTimestamp), BuilderFactory.STRING);
  }
