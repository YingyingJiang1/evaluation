  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScanParams that = (ScanParams) o;
    return Objects.equals(params, that.params);
  }
