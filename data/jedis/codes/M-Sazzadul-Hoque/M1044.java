  public final CommandObject<List<Long>> jsonArrInsertWithEscape(String key, Path2 path, int index, Object... objects) {
    CommandArguments args = commandArguments(JsonCommand.ARRINSERT).key(key).add(path).add(index);
    for (Object object : objects) {
      args.add(getJsonObjectMapper().toJson(object));
    }
    return new CommandObject<>(args, BuilderFactory.LONG_LIST);
  }
