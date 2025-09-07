  public FTSearchParams returnFields(String... fields) {
    if (returnFieldsNames == null) {
      returnFieldsNames = new ArrayList<>();
    }
    Arrays.stream(fields).forEach(f -> returnFieldsNames.add(FieldName.of(f)));
    return this;
  }
