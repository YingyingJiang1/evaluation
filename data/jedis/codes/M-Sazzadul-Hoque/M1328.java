  public static Reducer random_sample(String field, int size) {
    return new Reducer("RANDOM_SAMPLE", field) {
      @Override protected List<Object> getOwnArgs() {
        return Arrays.asList(size);
      }
    };
  }
