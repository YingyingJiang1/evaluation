  private CommandArguments addFlatMapArgs(CommandArguments args, Map<?, ?> map) {
    for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
      args.add(entry.getKey());
      args.add(entry.getValue());
    }
    return args;
  }
