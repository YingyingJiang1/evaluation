  public final CommandObject<KeyValue<byte[], List<byte[]>>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
    return new CommandObject<>(commandArguments(BLMPOP).blocking().add(timeout)
        .add(keys.length).keys((Object[]) keys).add(direction).add(COUNT).add(count),
        BuilderFactory.KEYED_BINARY_LIST);
  }
