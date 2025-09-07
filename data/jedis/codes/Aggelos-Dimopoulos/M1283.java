  private static Value[] fromStrings(String[] values) {
    Value[] objs = new Value[values.length];
    for (int i = 0; i < values.length; i++) {
      objs[i] = Values.value(values[i]);
    }
    return objs;
  }
