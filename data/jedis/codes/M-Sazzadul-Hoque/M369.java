  @Override
  public Object eval(final byte[] script, final List<byte[]> keys, final List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.eval(script, keys, args));
  }
