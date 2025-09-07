  public final CommandObject<Long> xtrim(byte[] key, long maxLen, boolean approximateLength) {
    CommandArguments args = commandArguments(XTRIM).key(key).add(MAXLEN);
    if (approximateLength) args.add(Protocol.BYTES_TILDE);
    args.add(maxLen);
    return new CommandObject<>(args, BuilderFactory.LONG);
  }
