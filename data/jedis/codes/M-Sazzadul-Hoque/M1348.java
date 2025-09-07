  public FTCreateParams addPrefix(String prefix) {
    if (this.prefix == null) {
      this.prefix = new ArrayList<>();
    }
    this.prefix.add(prefix);
    return this;
  }
