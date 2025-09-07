  public final CommandObject<Object> eval(String script, int keyCount, String... params) {
    return new CommandObject<>(commandArguments(EVAL).add(script).add(keyCount)
        .addObjects((Object[]) params).processKeys(Arrays.copyOf(params, keyCount)),
        BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
  }
