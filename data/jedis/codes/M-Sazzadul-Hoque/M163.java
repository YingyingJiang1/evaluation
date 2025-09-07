  @Override
  public Map.Entry<SearchResult, ProfilingInfo> ftProfileSearch(String indexName,
      FTProfileParams profileParams, Query query) {
    return executeCommand(commandObjects.ftProfileSearch(indexName, profileParams, query));
  }
