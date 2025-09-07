  @Deprecated
  public final CommandObject<Long> jsonArrAppend(String key, String path, JSONObject... objects) {
    CommandArguments args = commandArguments(JsonCommand.ARRAPPEND).key(key).add(path);
    for (Object object : objects) {
      args.add(object);
    }
    return new CommandObject<>(args, BuilderFactory.LONG);
  }
