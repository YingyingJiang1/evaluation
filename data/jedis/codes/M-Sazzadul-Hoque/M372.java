  @Override
  public Object eval(final byte[] script, final int keyCount, final byte[]... params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.eval(script, keyCount, params));
  }
