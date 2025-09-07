  @Deprecated
  public final CommandObject<Long> jsonArrInsert(String key, Path path, int index, Object... pojos) {
    CommandArguments args = commandArguments(JsonCommand.ARRINSERT).key(key).add(path).add(index);
    for (Object pojo : pojos) {
      args.add(getJsonObjectMapper().toJson(pojo));
    }
    return new CommandObject<>(args, BuilderFactory.LONG);
  }
