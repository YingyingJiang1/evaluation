  public static Value tags(String... tags) {
    if (tags.length == 0) {
      throw new IllegalArgumentException("Must have at least one tag");
    }
    StringJoiner sj = new StringJoiner(" | ");
    for (String s : tags) {
      sj.add(s);
    }
    return new Value() {
      @Override
      public String toString() {
        return "{" + sj.toString() + "}";
      }
    };
  }
