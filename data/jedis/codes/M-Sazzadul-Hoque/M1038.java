  @Deprecated
  public final CommandObject<Map<String, Object>> ftConfigGet(String option) {
    return new CommandObject<>(commandArguments(SearchCommand.CONFIG).add(SearchKeyword.GET).add(option),
        protocol == RedisProtocol.RESP3 ? BuilderFactory.AGGRESSIVE_ENCODED_OBJECT_MAP : BuilderFactory.ENCODED_OBJECT_MAP_FROM_PAIRS);
  }
