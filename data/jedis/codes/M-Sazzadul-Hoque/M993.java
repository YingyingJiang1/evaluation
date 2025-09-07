  public final CommandObject<Long> xtrim(String key, long maxLen, boolean approximate) {
    CommandArguments args = commandArguments(XTRIM).key(key).add(MAXLEN);
    if (approximate) args.add(Protocol.BYTES_TILDE);
    args.add(maxLen);
    return new CommandObject<>(args, BuilderFactory.LONG);
  }
