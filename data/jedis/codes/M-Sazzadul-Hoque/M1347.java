  public FTCreateParams prefix(String... prefixes) {
    if (this.prefix == null) {
      this.prefix = new ArrayList<>(prefixes.length);
    }
    Arrays.stream(prefixes).forEach(p -> this.prefix.add(p));
    return this;
  }
