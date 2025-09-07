  public AggregationBuilder apply(String projection, String alias) {
    aggrArgs.add(SearchKeyword.APPLY);
    aggrArgs.add(projection);
    aggrArgs.add(SearchKeyword.AS);
    aggrArgs.add(alias);
    return this;
  }
