  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FailoverParams that = (FailoverParams) o;
    return force == that.force && Objects.equals(to, that.to) && Objects.equals(timeout, that.timeout);
  }
