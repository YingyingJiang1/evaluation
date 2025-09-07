  @Override
  public Map.Entry<AggregationResult, ProfilingInfo> ftProfileAggregate(String indexName,
      FTProfileParams profileParams, AggregationBuilder aggr) {
    return executeCommand(commandObjects.ftProfileAggregate(indexName, profileParams, aggr));
  }
