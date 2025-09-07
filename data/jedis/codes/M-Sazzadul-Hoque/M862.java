  @Override
  public void addParams(CommandArguments args) {
    if (compression != null) {
      args.add(COMPRESSION).add(toByteArray(compression));
    }
    if (override) {
      args.add(OVERRIDE);
    }
  }
