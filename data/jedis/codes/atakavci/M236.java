  @Override
  public void addParams(CommandArguments args) {
    if (expiration != null) {
      args.add(expiration);
      if (expirationValue != null) {
        args.add(expirationValue);
      }
    }
  }
