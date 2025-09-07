  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(VECTOR);

    args.add(algorithm);
    args.add(attributes.size() << 1);
    attributes.forEach((name, value) -> args.add(name).add(value));

    if (indexMissing) {
      args.add(INDEXMISSING);
    }
  }
