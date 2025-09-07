  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XTrimParams that = (XTrimParams) o;
    return approximateTrimming == that.approximateTrimming && exactTrimming == that.exactTrimming && Objects.equals(maxLen, that.maxLen) && Objects.equals(minId, that.minId) && Objects.equals(limit, that.limit);
  }
