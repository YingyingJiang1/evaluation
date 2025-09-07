  @Override
  public String moduleLoadEx(String path, ModuleLoadExParams params) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(new CommandArguments(Command.MODULE).add(LOADEX).add(path)
        .addParams(params));
    return connection.getStatusCodeReply();
  }
