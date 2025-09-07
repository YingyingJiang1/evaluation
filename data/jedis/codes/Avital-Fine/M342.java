  @Override
  public void shutdown(ShutdownParams shutdownParams) throws JedisException {
    connection.sendCommand(new CommandArguments(SHUTDOWN).addParams(shutdownParams));
    try {
      throw new JedisException(connection.getStatusCodeReply());
    } catch (JedisConnectionException jce) {
      // expected
      connection.setBroken();
    }
  }
