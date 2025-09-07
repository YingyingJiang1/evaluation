  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName).add(GEOSHAPE).add(system);

    if (indexMissing) {
      args.add(INDEXMISSING);
    }

    if (noIndex) {
      args.add(NOINDEX);
    }
  }
