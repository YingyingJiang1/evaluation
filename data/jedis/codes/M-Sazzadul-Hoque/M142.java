  @Override
  @Deprecated
  public Response<String> ftConfigSet(String indexName, String option, String value) {
    return appendCommand(commandObjects.ftConfigSet(indexName, option, value));
  }
