  @Override
  public String configSetBinary(Map<byte[], byte[]> parameterValues) {
    checkIsInMultiOrPipeline();
    CommandArguments args = new CommandArguments(Command.CONFIG).add(Keyword.SET);
    parameterValues.forEach((k, v) -> args.add(k).add(v));
    connection.sendCommand(args);
    return connection.getStatusCodeReply();
  }
