  public final CommandObject<Object> eval(byte[] script, int keyCount, byte[]... params) {
    return new CommandObject<>(commandArguments(EVAL).add(script).add(keyCount)
        .addObjects((Object[]) params).processKeys(Arrays.copyOf(params, keyCount)),
        BuilderFactory.RAW_OBJECT);
  }
