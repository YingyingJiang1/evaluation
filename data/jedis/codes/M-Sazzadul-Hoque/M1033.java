  public final CommandObject<Map.Entry<AggregationResult, ProfilingInfo>> ftProfileAggregate(
      String indexName, FTProfileParams profileParams, AggregationBuilder aggr) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.PROFILE, indexName)
        .add(SearchKeyword.AGGREGATE).addParams(profileParams).add(SearchKeyword.QUERY)
        .addParams(aggr.dialectOptional(searchDialect.get())), new SearchProfileResponseBuilder<>(
        !aggr.isWithCursor() ? AggregationResult.SEARCH_AGGREGATION_RESULT
        : AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR));
  }
