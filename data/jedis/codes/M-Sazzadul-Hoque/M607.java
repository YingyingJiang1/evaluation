  @Override
  public List<Long> lpos(final String key, final String element, final LPosParams params,
      final long count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpos(key, element, params, count));
  }
