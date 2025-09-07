  @Override
  public Map.Entry<SearchResult, ProfilingInfo> ftProfileSearch(String indexName,
      FTProfileParams profileParams, String query, FTSearchParams searchParams) {
    return executeCommand(commandObjects.ftProfileSearch(indexName, profileParams, query, searchParams));
  }
