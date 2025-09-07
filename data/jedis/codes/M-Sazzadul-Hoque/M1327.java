  public static Reducer first_value(String field, SortedField sortBy) {
    return new Reducer("FIRST_VALUE", field) {
      @Override protected List<Object> getOwnArgs() {
        return Arrays.asList("BY", sortBy.getField(), sortBy.getOrder());
      }
    };
  }
