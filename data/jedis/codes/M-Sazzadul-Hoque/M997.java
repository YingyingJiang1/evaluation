  public final CommandObject<List<Object>> xautoclaim(byte[] key, byte[] groupName,
      byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
    return new CommandObject<>(commandArguments(XAUTOCLAIM).key(key).add(groupName)
        .add(consumerName).add(minIdleTime).add(start).addParams(params),
        BuilderFactory.RAW_OBJECT_LIST);
  }
