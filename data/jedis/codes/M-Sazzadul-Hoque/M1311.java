  public AggregationBuilder sortBy(int max, SortedField... fields) {
    sortBy(fields);
    sortByMax(max);
    return this;
  }
