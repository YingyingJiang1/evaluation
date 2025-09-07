  public final CommandObject<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
    return new CommandObject<>(commandArguments(EVALSHA).add(sha1).add(keyCount)
        .addObjects((Object[]) params).processKeys(Arrays.copyOf(params, keyCount)),
        BuilderFactory.RAW_OBJECT);
  }
