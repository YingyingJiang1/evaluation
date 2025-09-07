  @Override
  public Object evalReadonly(byte[] script, List<byte[]> keys, List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalReadonly(script, keys, args));
  }
