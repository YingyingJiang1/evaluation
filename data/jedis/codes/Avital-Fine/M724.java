  @Override
  public Object evalshaReadonly(String sha1, List<String> keys, List<String> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalshaReadonly(sha1, keys, args));
  }
