  @Override
  public Object eval(final byte[] script) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.eval(script));
  }
