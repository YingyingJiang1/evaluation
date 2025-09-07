  public List<String> pubsubShardChannels() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, SHARDCHANNELS);
    return connection.getMultiBulkReply();
  }
