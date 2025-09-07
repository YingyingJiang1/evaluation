  public AggregationBuilder groupBy(Group group) {
    aggrArgs.add(SearchKeyword.GROUPBY);
    group.addArgs(aggrArgs);
    return this;
  }
