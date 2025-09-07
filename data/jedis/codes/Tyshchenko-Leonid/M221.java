  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LCSParams lcsParams = (LCSParams) o;
    return len == lcsParams.len && idx == lcsParams.idx && withMatchLen == lcsParams.withMatchLen && Objects.equals(minMatchLen, lcsParams.minMatchLen);
  }
