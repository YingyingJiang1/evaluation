  public final CommandObject<SearchResult> ftSearch(String indexName, String query, FTSearchParams params) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.SEARCH, indexName)
        .add(query).addParams(params.dialectOptional(searchDialect.get())),
        getSearchResultBuilder(params.getReturnFieldDecodeMap(), () -> new SearchResultBuilder(
            !params.getNoContent(), params.getWithScores(), true, params.getReturnFieldDecodeMap())));
  }
