  public Object sendCommand(ProtocolCommand cmd, byte[]... args) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(cmd, args);
    return connection.getOne();
  }
