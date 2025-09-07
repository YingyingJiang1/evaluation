  @Override
  public final void multi() {
    connection.sendCommand(MULTI);
    String status = connection.getStatusCodeReply();
    if (!"OK".equals(status)) {
      throw new JedisException("MULTI command failed. Received response: " + status);
    }
    inMulti = true;
  }
