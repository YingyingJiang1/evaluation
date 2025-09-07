  @Override
  public String tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long bucketDuration, long alignTimestamp) {
    return executeCommand(
        commandObjects.tsCreateRule(sourceKey, destKey, aggregationType, bucketDuration, alignTimestamp));
  }
