  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZIncrByParams that = (ZIncrByParams) o;
    return existance == that.existance;
  }
