  public final CommandObject<List<Tuple>> ftSugGetWithScores(String key, String prefix, boolean fuzzy, int max) {
    CommandArguments args = commandArguments(SearchCommand.SUGGET).key(key).add(prefix);
    if (fuzzy) args.add(SearchKeyword.FUZZY);
    args.add(SearchKeyword.MAX).add(max);
    args.add(SearchKeyword.WITHSCORES);
    return new CommandObject<>(args, BuilderFactory.TUPLE_LIST);
  }
