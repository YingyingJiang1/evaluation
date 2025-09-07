  @Override
  public void shutdown() throws JedisException {
    connection.sendCommand(SHUTDOWN);
    try {
      throw new JedisException(connection.getStatusCodeReply());
    } catch (JedisConnectionException jce) {
      // expected
      connection.setBroken();
    }
  }
