  @Override
  @Deprecated
  public Map<String, Object> ftConfigGet(String indexName, String option) {
    return executeCommand(commandObjects.ftConfigGet(indexName, option));
  }
