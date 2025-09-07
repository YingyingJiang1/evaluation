  @Override
  public List<Tuple> zinterWithScores(final ZParams params, final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zinterWithScores(params, keys));
  }
