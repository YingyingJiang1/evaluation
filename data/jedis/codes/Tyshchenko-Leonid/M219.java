  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZRangeParams that = (ZRangeParams) o;
    return rev == that.rev && limit == that.limit && offset == that.offset && count == that.count && by == that.by && Objects.equals(min, that.min) && Objects.equals(max, that.max);
  }
