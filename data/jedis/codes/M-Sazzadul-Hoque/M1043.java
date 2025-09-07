  @Deprecated
  public final CommandObject<Long> jsonArrAppend(String key, Path path, Object... pojos) {
    CommandArguments args = commandArguments(JsonCommand.ARRAPPEND).key(key).add(path);
    for (Object pojo : pojos) {
      args.add(getJsonObjectMapper().toJson(pojo));
    }
    return new CommandObject<>(args, BuilderFactory.LONG);
  }
