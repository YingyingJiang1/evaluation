  public FTSearchParams returnField(FieldName field, boolean decode) {
    returnFields(field);
    addReturnFieldDecode(field.getAttribute() != null ? field.getAttribute() : field.getName(), decode);
    return this;
  }
