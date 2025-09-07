  public AggregationBuilder cursor(int count, long maxIdle) {
    isWithCursor = true;
    aggrArgs.add(SearchKeyword.WITHCURSOR);
    aggrArgs.add(SearchKeyword.COUNT);
    aggrArgs.add(count);
    aggrArgs.add(SearchKeyword.MAXIDLE);
    aggrArgs.add(maxIdle);
    return this;
  }
