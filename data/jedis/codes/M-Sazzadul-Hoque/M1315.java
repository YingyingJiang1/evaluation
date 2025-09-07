  public AggregationBuilder filter(String expression) {
    aggrArgs.add(SearchKeyword.FILTER);
    aggrArgs.add(expression);
    return this;
  }
