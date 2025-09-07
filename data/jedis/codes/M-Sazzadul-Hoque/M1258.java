  public Query returnFields(FieldName... fields) {
    this.returnFieldNames = fields;
    this._returnFields = null;
    return this;
  }
