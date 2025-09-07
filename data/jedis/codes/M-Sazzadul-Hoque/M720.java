  public List<String> pubsubShardChannels(final String pattern) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, SHARDCHANNELS.name(), pattern);
    return connection.getMultiBulkReply();
  }
