  @Override
  @Deprecated
  public Response<String> ftConfigSet(String option, String value) {
    return appendCommand(commandObjects.ftConfigSet(option, value));
  }
