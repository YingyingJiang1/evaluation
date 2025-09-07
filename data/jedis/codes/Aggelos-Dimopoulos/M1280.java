  public static Value value(String s) {
    return new ScalableValue() {
      @Override
      public String toString() {
        return s;
      }
    };
  }
