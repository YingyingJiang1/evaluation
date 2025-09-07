  @Override
  public String functionFlush(final FlushMode mode) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionFlush(mode));
  }
