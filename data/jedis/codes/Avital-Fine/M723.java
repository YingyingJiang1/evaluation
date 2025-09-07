  @Override
  public Object evalReadonly(String script, List<String> keys, List<String> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalReadonly(script, keys, args));
  }
