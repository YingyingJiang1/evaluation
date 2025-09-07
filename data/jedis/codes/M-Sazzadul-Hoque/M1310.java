  public AggregationBuilder sortByMax(int max) {
    aggrArgs.add(SearchKeyword.MAX);
    aggrArgs.add(max);
    return this;
  }
