  public void monitor(final JedisMonitor jedisMonitor) {
//    connection.monitor();
    connection.sendCommand(Command.MONITOR);
    connection.getStatusCodeReply();
    jedisMonitor.proceed(connection);
  }
