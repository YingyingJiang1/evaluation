  @Override
  public String getEx(String key, GetExParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.getEx(key, params));
  }
