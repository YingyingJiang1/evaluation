  public final CommandObject<Boolean> copy(String srcKey, String dstKey, int dstDB, boolean replace) {
    CommandArguments args = commandArguments(Command.COPY).key(srcKey).key(dstKey).add(DB).add(dstDB);
    if (replace) args.add(REPLACE);
    return new CommandObject<>(args, BuilderFactory.BOOLEAN);
  }
