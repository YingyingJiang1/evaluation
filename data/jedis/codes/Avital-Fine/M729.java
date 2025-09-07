  @Override
  public List<KeyValue<String, List<String>>> commandGetKeysAndFlags(String... command) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(COMMAND, joinParameters(GETKEYSANDFLAGS.name(), command));
    return BuilderFactory.KEYED_STRING_LIST_LIST.build(connection.getOne());
  }
