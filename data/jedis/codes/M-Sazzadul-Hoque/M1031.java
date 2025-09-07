  public final CommandObject<AggregationResult> ftAggregate(String indexName, AggregationBuilder aggr) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.AGGREGATE, indexName)
        .addParams(aggr.dialectOptional(searchDialect.get())), !aggr.isWithCursor() ? AggregationResult.SEARCH_AGGREGATION_RESULT
        : AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR);
  }
