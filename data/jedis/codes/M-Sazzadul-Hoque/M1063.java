  private CommandArguments addFlatKeyValueArgs(CommandArguments args, byte[]... keyvalues) {
    for (int i = 0; i < keyvalues.length; i += 2) {
      args.key(keyvalues[i]).add(keyvalues[i + 1]);
    }
    return args;
  }
