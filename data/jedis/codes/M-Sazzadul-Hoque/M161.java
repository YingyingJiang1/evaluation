  @Override
  @Deprecated
  public SearchResult ftSearch(byte[] indexName, Query query) {
    return executeCommand(commandObjects.ftSearch(indexName, query));
  }
