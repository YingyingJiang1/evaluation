  @Override
  public void addParams(CommandArguments args) {

    if (to != null) {
      args.add(Keyword.TO).add(to.getHost()).add(to.getPort());
    }

    if (force) {
      if (to == null || timeout == null) {
        throw new IllegalArgumentException("FAILOVER with force option requires both a timeout and target HOST and IP.");
      }
      args.add(Keyword.FORCE);
    }

    if (timeout != null) {
      args.add(Keyword.TIMEOUT).add(timeout);
    }

  }
