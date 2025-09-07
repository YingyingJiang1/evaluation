  public FTSearchParams returnFields(Collection<FieldName> fields) {
    if (returnFieldsNames == null) {
      returnFieldsNames = new ArrayList<>();
    }
    returnFieldsNames.addAll(fields);
    return this;
  }
