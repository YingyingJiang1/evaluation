  public final CommandObject<Map.Entry<StreamEntryID, List<StreamEntryID>>> xautoclaimJustId(
      String key, String group, String consumerName, long minIdleTime, StreamEntryID start,
      XAutoClaimParams params) {
    return new CommandObject<>(commandArguments(XAUTOCLAIM).key(key).add(group)
        .add(consumerName).add(minIdleTime).add(start).addParams(params)
        .add(JUSTID), BuilderFactory.STREAM_AUTO_CLAIM_JUSTID_RESPONSE);
  }
