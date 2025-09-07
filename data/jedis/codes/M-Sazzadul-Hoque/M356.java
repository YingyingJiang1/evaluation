  @Override
  public String configSet(final byte[]... parameterValues) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, joinParameters(Keyword.SET.getRaw(), parameterValues));
    return connection.getStatusCodeReply();
  }
