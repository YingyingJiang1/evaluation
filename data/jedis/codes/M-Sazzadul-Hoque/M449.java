  @Override
  public String pfmerge(final byte[] destkey, final byte[]... sourcekeys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pfmerge(destkey, sourcekeys));
  }
