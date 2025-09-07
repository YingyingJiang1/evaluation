  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ShutdownParams that = (ShutdownParams) o;
    return now == that.now && force == that.force && saveMode == that.saveMode;
  }
