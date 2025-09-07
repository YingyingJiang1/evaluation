  public AggregationBuilder limit(int offset, int count) {
    aggrArgs.add(SearchKeyword.LIMIT);
    aggrArgs.add(offset);
    aggrArgs.add(count);
    return this;
  }
