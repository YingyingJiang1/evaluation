  public AggregationBuilder cursor(int count) {
    isWithCursor = true;
    aggrArgs.add(SearchKeyword.WITHCURSOR);
    aggrArgs.add(SearchKeyword.COUNT);
    aggrArgs.add(count);
    return this;
  }
