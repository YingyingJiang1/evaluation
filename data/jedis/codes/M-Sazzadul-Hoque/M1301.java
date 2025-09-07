  @Override
  public void addParams(CommandArguments args) {

    if (terms != null) {
      terms.forEach(kv -> args.add(TERMS).add(kv.getValue()).add(kv.getKey()));
    }

    if (distance != null) {
      args.add(DISTANCE).add(distance);
    }

    if (dialect != null) {
      args.add(DIALECT).add(dialect);
    }
  }
