  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XAutoClaimParams that = (XAutoClaimParams) o;
    return Objects.equals(count, that.count);
  }
