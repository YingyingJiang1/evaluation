  @Override
  public String configSet(final byte[] parameter, final byte[] value) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, Keyword.SET.getRaw(), parameter, value);
    return connection.getStatusCodeReply();
  }
