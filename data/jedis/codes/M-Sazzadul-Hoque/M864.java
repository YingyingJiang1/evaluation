  @Override
  public void addParams(CommandArguments args) {
    if (capacity != null) {
      args.add(CAPACITY).add(toByteArray(capacity));
    }
    if (errorRate != null) {
      args.add(ERROR).add(toByteArray(errorRate));
    }
    if (expansion != null) {
      args.add(EXPANSION).add(toByteArray(expansion));
    }
    if (noCreate) {
      args.add(NOCREATE);
    }
    if (nonScaling) {
      args.add(NONSCALING);
    }
  }
