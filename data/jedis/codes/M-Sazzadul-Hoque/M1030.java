  @Deprecated
  public final CommandObject<SearchResult> ftSearch(byte[] indexName, Query query) {
    if (protocol == RedisProtocol.RESP3) {
      throw new UnsupportedOperationException("binary ft.search is not implemented with resp3.");
    }
    return new CommandObject<>(checkAndRoundRobinSearchCommand(commandArguments(SearchCommand.SEARCH), indexName)
        .addParams(query.dialectOptional(searchDialect.get())), getSearchResultBuilder(null,
        () -> new SearchResultBuilder(!query.getNoContent(), query.getWithScores(), false)));
  }
