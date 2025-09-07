  @Override
  public Object eval(final String script, final int keyCount, final String... params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.eval(script, keyCount, params));
  }
