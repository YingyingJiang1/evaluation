  @Override
  public void addParams(CommandArguments args) {
    if (idleTime != null) {
      args.add(Keyword.IDLE).add(idleTime);
    }
    if (idleUnixTime != null) {
      args.add(Keyword.TIME).add(idleUnixTime);
    }
    if (retryCount != null) {
      args.add(Keyword.RETRYCOUNT).add(retryCount);
    }
    if (force) {
      args.add(Keyword.FORCE);
    }
  }
