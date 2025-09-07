  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(GEO);

    if (indexMissing) {
      args.add(INDEXMISSING);
    }

    if (sortable) {
      args.add(SORTABLE);
    }

    if (noIndex) {
      args.add(NOINDEX);
    }
  }
