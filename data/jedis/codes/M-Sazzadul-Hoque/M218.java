  @Override
  public void addParams(CommandArguments args) {

    args.add(min).add(max);
    if (by != null) {
      args.add(by);
    }

    if (rev) {
      args.add(Keyword.REV);
    }

    if (limit) {
      args.add(Keyword.LIMIT).add(offset).add(count);
    }
  }
