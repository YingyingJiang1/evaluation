  @Override
  public void addParams(CommandArguments args) {

    if (limited) {
      args.add(LIMITED);
    }
  }
