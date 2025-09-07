  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseSetExParams setParams = (BaseSetExParams) o;
    return Objects.equals(expiration, setParams.expiration) 
      && Objects.equals(expirationValue, setParams.expirationValue);
  }
