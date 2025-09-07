  @Override
  @Deprecated
  public Response<Map<String, Object>> ftConfigGet(String option) {
    return appendCommand(commandObjects.ftConfigGet(option));
  }
