  public final void addArgs(List<Object> args) {

    args.add(SearchKeyword.REDUCE);
    args.add(name);

    List<Object> ownArgs = getOwnArgs();
    if (field != null) {
      args.add(1 + ownArgs.size());
      args.add(field);
    } else {
      args.add(ownArgs.size());
    }
    args.addAll(ownArgs);

    if (alias != null) {
      args.add(SearchKeyword.AS);
      args.add(alias);
    }
  }
