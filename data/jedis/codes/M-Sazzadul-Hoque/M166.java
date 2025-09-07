  @Override
  @Deprecated
  public Map<String, Object> ftConfigGet(String option) {
    return executeCommand(commandObjects.ftConfigGet(option));
  }
