  @Override
  public String moduleLoad(String path, String... args) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.MODULE, joinParameters(LOAD.name(), path, args));
    return connection.getStatusCodeReply();
  }
