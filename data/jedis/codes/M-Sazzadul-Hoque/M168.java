  @Override
  @Deprecated
  public String ftConfigSet(String option, String value) {
    return executeCommand(commandObjects.ftConfigSet(option, value));
  }
