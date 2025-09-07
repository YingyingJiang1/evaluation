  public final CommandObject<Object> evalsha(String sha1, int keyCount, String... params) {
    return new CommandObject<>(commandArguments(EVALSHA).add(sha1).add(keyCount)
        .addObjects((Object[]) params).processKeys(Arrays.copyOf(params, keyCount)),
        BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
  }
