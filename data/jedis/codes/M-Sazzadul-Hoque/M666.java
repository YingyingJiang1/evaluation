  @Override
  public String lmove(final String srcKey, final String dstKey, final ListDirection from,
      final ListDirection to) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmove(srcKey, dstKey, from, to));
  }
