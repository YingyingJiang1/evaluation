  public final CommandObject<SearchResult> ftSearch(String indexName, Query query) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.SEARCH, indexName)
        .addParams(query.dialectOptional(searchDialect.get())), getSearchResultBuilder(null,
        () -> new SearchResultBuilder(!query.getNoContent(), query.getWithScores(), true)));
  }
