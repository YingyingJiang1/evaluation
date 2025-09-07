  public AggregationBuilder groupBy(Collection<String> fields, Collection<Reducer> reducers) {
    String[] fieldsArr = new String[fields.size()];
    Group g = new Group(fields.toArray(fieldsArr));
    reducers.forEach((r) -> g.reduce(r));
    groupBy(g);
    return this;
  }
