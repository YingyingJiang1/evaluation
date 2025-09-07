  private FTSpellCheckParams addTerm(String dictionary, Rawable type) {
    if (this.terms == null) {
      this.terms = new ArrayList<>();
    }
    this.terms.add(KeyValue.of(dictionary, type));
    return this;
  }
