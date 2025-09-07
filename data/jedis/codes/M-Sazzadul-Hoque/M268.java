  @Override
  public String select(final int index) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(SELECT, toByteArray(index));
    String statusCodeReply = connection.getStatusCodeReply();
    this.db = index;
    return statusCodeReply;
  }
