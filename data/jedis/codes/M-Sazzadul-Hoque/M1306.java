  public AggregationBuilder load(FieldName... fields) {
    aggrArgs.add(SearchKeyword.LOAD);
    LazyRawable rawLoadCount = new LazyRawable();
    aggrArgs.add(rawLoadCount);
    int loadCount = 0;
    for (FieldName fn : fields) {
      loadCount += fn.addCommandArguments(aggrArgs);
    }
    rawLoadCount.setRaw(Protocol.toByteArray(loadCount));
    return this;
  }
