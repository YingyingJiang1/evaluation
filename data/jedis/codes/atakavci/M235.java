  private T expiration(Keyword type, Long value) {
    this.expiration = type;
    this.expirationValue = value;
    return (T) this;
  }
