  @Override
  public Map<String, CommandDocument> commandDocs(String... commands) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(COMMAND, joinParameters(DOCS.name(), commands));
    return BuilderFactory.COMMAND_DOCS_RESPONSE.build(connection.getOne());
  }
