  public String watch(final String... keys) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(WATCH, keys);
//    return connection.getStatusCodeReply();
    String status = connection.getStatusCodeReply();
    isInWatch = true;
    return status;
  }
