  public final CommandObject<List<Long>> cmsIncrBy(String key, Map<String, Long> itemIncrements) {
    CommandArguments args = commandArguments(CountMinSketchCommand.INCRBY).key(key);
    itemIncrements.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.LONG_LIST);
  }
