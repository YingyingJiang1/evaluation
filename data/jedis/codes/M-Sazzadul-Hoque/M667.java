  @Override
  public String blmove(final String srcKey, final String dstKey, final ListDirection from,
      final ListDirection to, final double timeout) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmove(srcKey, dstKey, from, to, timeout));
  }
