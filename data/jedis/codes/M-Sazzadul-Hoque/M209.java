  @Override
  public void addParams(CommandArguments args) {
    if (count != null) {
      args.add(Keyword.COUNT).add(count);
    }
    if (block != null) {
      args.add(Keyword.BLOCK).add(block).blocking();
    }
  }
