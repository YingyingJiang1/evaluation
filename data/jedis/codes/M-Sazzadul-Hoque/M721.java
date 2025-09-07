  public Map<String, Long> pubsubShardNumSub(String... channels) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(PUBSUB, joinParameters(SHARDNUMSUB.name(), channels));
    return BuilderFactory.PUBSUB_NUMSUB_MAP.build(connection.getOne());
  }
