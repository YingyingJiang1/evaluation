  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LPosParams that = (LPosParams) o;
    return Objects.equals(rank, that.rank) && Objects.equals(maxlen, that.maxlen);
  }
