  @Override
  @Deprecated
  public Response<Map<String, Object>> ftConfigGet(String indexName, String option) {
    return appendCommand(commandObjects.ftConfigGet(indexName, option));
  }
