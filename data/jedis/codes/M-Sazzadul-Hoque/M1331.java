  public FTSearchParams inFields(Collection<String> fields) {
    if (this.inFields == null) {
      this.inFields = new ArrayList<>(fields);
    } else {
      this.inFields.addAll(fields);
    }
    return this;
  }
