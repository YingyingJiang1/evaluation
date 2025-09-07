  @Override
  protected CommandArguments initCommandArguments() {
    batchStart = 0;
    return args.apply(batchStart);
  }
