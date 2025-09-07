  @Override
  public Map<byte[], byte[]> configGet(final byte[] pattern) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, Keyword.GET.getRaw(), pattern);
    return BuilderFactory.BINARY_MAP.build(connection.getOne());
  }
