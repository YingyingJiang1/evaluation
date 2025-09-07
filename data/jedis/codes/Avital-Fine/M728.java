  @Override
  public List<String> commandGetKeys(String... command) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(COMMAND, joinParameters(GETKEYS.name(), command));
    return BuilderFactory.STRING_LIST.build(connection.getOne());
  }
