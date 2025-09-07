  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XAddParams that = (XAddParams) o;
    return approximateTrimming == that.approximateTrimming && exactTrimming == that.exactTrimming && nomkstream == that.nomkstream && Objects.equals(id, that.id) && Objects.equals(maxLen, that.maxLen) && Objects.equals(minId, that.minId) && Objects.equals(limit, that.limit);
  }
