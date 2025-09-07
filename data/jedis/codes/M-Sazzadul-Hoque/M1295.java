  public int addCommandArguments(List<Object> args) {
    args.add(name);
    if (attribute == null) {
      return 1;
    }

    args.add(SearchKeyword.AS);
    args.add(attribute);
    return 3;
  }
