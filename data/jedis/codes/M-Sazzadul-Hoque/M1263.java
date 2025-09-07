  public Query dialectOptional(int dialect) {
    if (dialect != 0 && this._dialect == null) {
      this._dialect = dialect;
    }
    return this;
  }
