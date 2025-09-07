  public static Reducer quantile(String field, double percentile) {
    return new Reducer("QUANTILE", field) {
      @Override protected List<Object> getOwnArgs() {
        return Arrays.asList(percentile);
      }
    };
  }
