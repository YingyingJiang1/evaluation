  private static Reducer singleFieldReducer(String name, String field) {
    return new Reducer(name, field) {
      @Override protected List<Object> getOwnArgs() {
        return Collections.emptyList();
      }
    };
  }
