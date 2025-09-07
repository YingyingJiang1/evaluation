  @Override
  public String unwatch() {
    connection.sendCommand(UNWATCH);
    String status = connection.getStatusCodeReply();
    inWatch = false;
    return status;
  }
