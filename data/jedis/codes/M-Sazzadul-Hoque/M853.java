  public Object sendBlockingCommand(ProtocolCommand cmd, String... args) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(cmd, args);
    connection.setTimeoutInfinite();
    try {
      return connection.getOne();
    } finally {
      connection.rollbackTimeout();
    }
  }
