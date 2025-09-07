  @Override
  public final CommandObject<Set<byte[]>> keys(byte[] pattern) {
    if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
      throw new IllegalArgumentException(KEYS_PATTERN_MESSAGE);
    }
    return new CommandObject<>(commandArguments(KEYS).key(pattern).processKey(pattern), BuilderFactory.BINARY_SET);
  }
