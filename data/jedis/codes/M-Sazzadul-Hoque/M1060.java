  private CommandArguments addFlatArgs(CommandArguments args, long... values) {
    for (long value : values) {
      args.add(value);
    }
    return args;
  }
