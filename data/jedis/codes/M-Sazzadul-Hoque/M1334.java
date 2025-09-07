  public FTSearchParams returnField(String field, boolean decode) {
    returnFields(field);
    addReturnFieldDecode(field, decode);
    return this;
  }
