  public final CommandObject<Boolean> copy(byte[] srcKey, byte[] dstKey, boolean replace) {
    CommandArguments args = commandArguments(Command.COPY).key(srcKey).key(dstKey);
    if (replace) {
      args.add(REPLACE);
    }
    return new CommandObject<>(args, BuilderFactory.BOOLEAN);
  }
