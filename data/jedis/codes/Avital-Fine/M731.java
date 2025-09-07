  @Override
  public List<String> commandList() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(COMMAND, LIST);
    return BuilderFactory.STRING_LIST.build(connection.getOne());
  }
