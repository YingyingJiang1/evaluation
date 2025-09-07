  private CommandArguments addFlatArgs(CommandArguments args, double... values) {
    for (double value : values) {
      args.add(value);
    }
    return args;
  }
