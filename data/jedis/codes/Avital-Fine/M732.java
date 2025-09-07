  @Override
  public List<String> commandListFilterBy(CommandListFilterByParams filterByParams) {
    checkIsInMultiOrPipeline();
    CommandArguments args = new CommandArguments(COMMAND).add(LIST).addParams(filterByParams);
    connection.sendCommand(args);
    return BuilderFactory.STRING_LIST.build(connection.getOne());
  }
