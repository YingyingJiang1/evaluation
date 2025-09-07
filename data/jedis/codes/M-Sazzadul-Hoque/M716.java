  public List<String> pubsubChannels() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, CHANNELS);
    return connection.getMultiBulkReply();
  }
