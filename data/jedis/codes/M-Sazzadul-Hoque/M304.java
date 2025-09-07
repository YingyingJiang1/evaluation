  public String watch(final byte[]... keys) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(WATCH, keys);
//    return connection.getStatusCodeReply();
    String status = connection.getStatusCodeReply();
    isInWatch = true;
    return status;
  }
