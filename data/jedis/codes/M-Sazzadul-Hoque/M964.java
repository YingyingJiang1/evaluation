  public final CommandObject<KeyValue<String, List<String>>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
    return new CommandObject<>(commandArguments(BLMPOP).blocking().add(timeout)
        .add(keys.length).keys((Object[]) keys).add(direction).add(COUNT).add(count),
        BuilderFactory.KEYED_STRING_LIST);
  }
