  @Override
  @Deprecated
  public Response<SearchResult> ftSearch(byte[] indexName, Query query) {
    return appendCommand(commandObjects.ftSearch(indexName, query));
  }
