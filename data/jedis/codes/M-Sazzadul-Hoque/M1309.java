  public AggregationBuilder sortBy(SortedField... fields) {
    aggrArgs.add(SearchKeyword.SORTBY);
    aggrArgs.add(fields.length << 1);
    for (SortedField field : fields) {
      aggrArgs.add(field.getField());
      aggrArgs.add(field.getOrder());
    }
    return this;
  }
