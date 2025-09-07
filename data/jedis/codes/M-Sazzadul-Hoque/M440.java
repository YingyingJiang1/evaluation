  public List<String> time() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.TIME);
    return connection.getMultiBulkReply();
  }
