  @Override
  public void addParams(CommandArguments args) {
    if (bucketSize != null) {
      args.add(BUCKETSIZE).add(toByteArray(bucketSize));
    }
    if (maxIterations != null) {
      args.add(MAXITERATIONS).add(toByteArray(maxIterations));
    }
    if (expansion != null) {
      args.add(EXPANSION).add(toByteArray(expansion));
    }
  }
