  public List<String> pubsubChannels(final String pattern) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, CHANNELS.name(), pattern);
    return connection.getMultiBulkReply();
  }
