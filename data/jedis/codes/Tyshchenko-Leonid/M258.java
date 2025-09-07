  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XClaimParams that = (XClaimParams) o;
    return force == that.force && Objects.equals(idleTime, that.idleTime) && Objects.equals(idleUnixTime, that.idleUnixTime) && Objects.equals(retryCount, that.retryCount);
  }
