  public Object sendBlockingCommand(ProtocolCommand cmd, byte[]... args) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(cmd, args);
    connection.setTimeoutInfinite();
    try {
      return connection.getOne();
    } finally {
      connection.rollbackTimeout();
    }
  }
