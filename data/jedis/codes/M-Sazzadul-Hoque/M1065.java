  private CommandArguments addSortedSetFlatMapArgs(CommandArguments args, Map<?, Double> map) {
    for (Map.Entry<? extends Object, Double> entry : map.entrySet()) {
      args.add(entry.getValue());
      args.add(entry.getKey());
    }
    return args;
  }
