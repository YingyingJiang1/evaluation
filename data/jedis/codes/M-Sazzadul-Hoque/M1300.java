  public FTSpellCheckParams dialectOptional(int dialect) {
    if (dialect != 0 && this.dialect == null) {
      this.dialect = dialect;
    }
    return this;
  }
