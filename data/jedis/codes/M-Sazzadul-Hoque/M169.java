  @Override
  @Deprecated
  public String ftConfigSet(String indexName, String option, String value) {
    return executeCommand(commandObjects.ftConfigSet(indexName, option, value));
  }
