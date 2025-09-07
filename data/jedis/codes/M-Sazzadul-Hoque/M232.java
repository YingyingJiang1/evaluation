  @Override
  public void addParams(CommandArguments args) {
    if (storeDist) {
      args.add(Keyword.STOREDIST).key(key);
    } else if (store) {
      args.add(Keyword.STORE).key(key);
    } else {
      throw new IllegalArgumentException(this.getClass().getSimpleName()
          + " must has store or storedist option");
    }
  }
