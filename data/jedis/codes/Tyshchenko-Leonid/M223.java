  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BitPosParams that = (BitPosParams) o;
    return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(modifier, that.modifier);
  }
