  public Long pubsubNumPat() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, NUMPAT);
    return connection.getIntegerReply();
  }
