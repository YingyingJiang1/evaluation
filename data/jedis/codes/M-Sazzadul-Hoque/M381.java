  @Override
  public List<Boolean> scriptExists(final byte[]... sha1) {
    connection.sendCommand(SCRIPT, joinParameters(Keyword.EXISTS.getRaw(), sha1));
    return BuilderFactory.BOOLEAN_LIST.build(connection.getOne());
  }
