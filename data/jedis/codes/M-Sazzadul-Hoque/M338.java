  @Override
  public String bgsaveSchedule() {
    connection.sendCommand(BGSAVE, SCHEDULE);
    return connection.getStatusCodeReply();
  }
