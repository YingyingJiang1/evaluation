  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseGetExParams that = (BaseGetExParams) o;
    return expiration == that.expiration && Objects.equals(expirationValue, that.expirationValue);
  }
