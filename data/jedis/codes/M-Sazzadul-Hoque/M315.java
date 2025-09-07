  @Override
  public long zcount(final byte[] key, final double min, final double max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zcount(key, min, max));
  }
