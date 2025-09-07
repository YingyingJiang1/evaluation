  @Override
  public String swapDB(final int index1, final int index2) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(SWAPDB, toByteArray(index1), toByteArray(index2));
    return connection.getStatusCodeReply();
  }
