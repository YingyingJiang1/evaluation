  @Override
  public void addParams(CommandArguments args) {
    if (start != null) {
      args.add(start);
      if (end != null) {
        args.add(end);
        if (modifier != null) {
          args.add(modifier);
        }
      }
    }
  }
