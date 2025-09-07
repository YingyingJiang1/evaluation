  public static Reducer count() {
    return new Reducer("COUNT") {
      @Override protected List<Object> getOwnArgs() {
        return Collections.emptyList();
      }
    };
  }
