  public final CommandObject<List<String>> ftSugGet(String key, String prefix, boolean fuzzy, int max) {
    CommandArguments args = commandArguments(SearchCommand.SUGGET).key(key).add(prefix);
    if (fuzzy) args.add(SearchKeyword.FUZZY);
    args.add(SearchKeyword.MAX).add(max);
    return new CommandObject<>(args, BuilderFactory.STRING_LIST);
  }
