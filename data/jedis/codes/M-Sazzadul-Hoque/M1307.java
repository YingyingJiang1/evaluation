  public AggregationBuilder loadAll() {
    aggrArgs.add(SearchKeyword.LOAD);
    aggrArgs.add(Protocol.BYTES_ASTERISK);
    return this;
  }
