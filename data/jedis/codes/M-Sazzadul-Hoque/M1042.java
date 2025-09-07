  public final CommandObject<List<Long>> jsonArrAppendWithEscape(String key, Path2 path, Object... objects) {
    CommandArguments args = commandArguments(JsonCommand.ARRAPPEND).key(key).add(path);
    for (Object object : objects) {
      args.add(getJsonObjectMapper().toJson(object));
    }
    return new CommandObject<>(args, BuilderFactory.LONG_LIST);
  }
