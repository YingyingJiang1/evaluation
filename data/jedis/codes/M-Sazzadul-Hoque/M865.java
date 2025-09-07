  @Override
  public void addParams(CommandArguments args) {
    if (capacity != null) {
      args.add(CAPACITY).add(toByteArray(capacity));
    }
    if (noCreate) {
      args.add(NOCREATE);
    }
  }
