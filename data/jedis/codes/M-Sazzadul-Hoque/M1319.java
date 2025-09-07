  @Override
  public void addParams(CommandArguments commArgs) {
    commArgs.addObjects(aggrArgs);
    if (dialect != null) {
      commArgs.add(SearchKeyword.DIALECT).add(dialect);
    }
  }
