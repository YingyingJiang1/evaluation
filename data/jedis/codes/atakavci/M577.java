  @Override
  public long hsetex(String key, HSetExParams params, String field, String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hsetex(key, params, field, value));
  }
