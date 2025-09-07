  @Override
  public String configSet(final String... parameterValues) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, joinParameters(Keyword.SET.name(), parameterValues));
    return connection.getStatusCodeReply();
  }
