  public final CommandObject<Map.Entry<SearchResult, ProfilingInfo>> ftProfileSearch(
      String indexName, FTProfileParams profileParams, String query, FTSearchParams searchParams) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.PROFILE, indexName)
        .add(SearchKeyword.SEARCH).addParams(profileParams).add(SearchKeyword.QUERY).add(query)
        .addParams(searchParams.dialectOptional(searchDialect.get())),
        new SearchProfileResponseBuilder<>(getSearchResultBuilder(searchParams.getReturnFieldDecodeMap(),
            () -> new SearchResultBuilder(!searchParams.getNoContent(), searchParams.getWithScores(), true,
                searchParams.getReturnFieldDecodeMap()))));
  }
