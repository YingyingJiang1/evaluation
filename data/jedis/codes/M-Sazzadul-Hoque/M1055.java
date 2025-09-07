  public final CommandObject<List<String>> topkIncrBy(String key, Map<String, Long> itemIncrements) {
    CommandArguments args = commandArguments(TopKCommand.INCRBY).key(key);
    itemIncrements.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.STRING_LIST);
  }
