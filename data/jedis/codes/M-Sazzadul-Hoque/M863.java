  @Override
  public void addParams(CommandArguments args) {
    if (expansion != null) {
      args.add(EXPANSION).add(toByteArray(expansion));
    }
    if (nonScaling) {
      args.add(NONSCALING);
    }
  }
