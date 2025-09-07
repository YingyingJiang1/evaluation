  public final CommandObject<AggregationResult> ftCursorRead(String indexName, long cursorId, int count) {
    return new CommandObject<>(commandArguments(SearchCommand.CURSOR).add(SearchKeyword.READ)
        .key(indexName).add(cursorId).add(SearchKeyword.COUNT).add(count),
        AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR);
  }
