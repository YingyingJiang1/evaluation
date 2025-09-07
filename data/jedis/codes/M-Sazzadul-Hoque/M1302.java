  public void addArgs(List<Object> args) {

    args.add(fields.size());
    args.addAll(fields);

    reducers.forEach((r) -> r.addArgs(args));
  }
