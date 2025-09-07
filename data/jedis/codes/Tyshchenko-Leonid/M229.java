  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZParams zParams = (ZParams) o;
    return Objects.equals(params, zParams.params);
  }
