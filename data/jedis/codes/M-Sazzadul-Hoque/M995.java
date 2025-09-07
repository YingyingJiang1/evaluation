  public final CommandObject<Map.Entry<StreamEntryID, List<StreamEntry>>> xautoclaim(String key,
      String group, String consumerName, long minIdleTime, StreamEntryID start,
      XAutoClaimParams params) {
    return new CommandObject<>(commandArguments(XAUTOCLAIM).key(key).add(group)
        .add(consumerName).add(minIdleTime).add(start).addParams(params),
        BuilderFactory.STREAM_AUTO_CLAIM_RESPONSE);
  }
