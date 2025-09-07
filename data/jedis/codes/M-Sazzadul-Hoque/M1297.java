  public static FieldName[] convert(String... names) {
    if (names == null) {
      return null;
    }
    FieldName[] fields = new FieldName[names.length];
    for (int i = 0; i < names.length; i++) {
      fields[i] = FieldName.of(names[i]);
    }
    return fields;
  }
