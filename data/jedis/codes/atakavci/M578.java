  @Override
  public long hsetex(String key, HSetExParams params, Map<String, String> hash) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hsetex(key, params, hash));
  }
