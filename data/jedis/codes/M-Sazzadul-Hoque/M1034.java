  public final CommandObject<Map.Entry<SearchResult, ProfilingInfo>> ftProfileSearch(
      String indexName, FTProfileParams profileParams, Query query) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.PROFILE, indexName)
        .add(SearchKeyword.SEARCH).addParams(profileParams).add(SearchKeyword.QUERY)
        .addParams(query.dialectOptional(searchDialect.get())),
        new SearchProfileResponseBuilder<>(getSearchResultBuilder(null,
            () -> new SearchResultBuilder(!query.getNoContent(), query.getWithScores(), true))));
  }
