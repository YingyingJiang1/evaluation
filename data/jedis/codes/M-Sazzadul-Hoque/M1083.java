  @Override
  public final CommandObject<Set<String>> keys(String pattern) {
    if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
      throw new IllegalArgumentException(KEYS_PATTERN_MESSAGE);
    }
    return new CommandObject<>(commandArguments(KEYS).key(pattern).processKey(pattern), BuilderFactory.STRING_SET);
  }
