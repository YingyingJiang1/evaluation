  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RestoreParams that = (RestoreParams) o;
    return replace == that.replace && absTtl == that.absTtl && Objects.equals(idleTime, that.idleTime) && Objects.equals(frequency, that.frequency);
  }
