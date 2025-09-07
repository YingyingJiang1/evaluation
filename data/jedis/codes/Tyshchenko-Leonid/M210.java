  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XReadParams that = (XReadParams) o;
    return Objects.equals(count, that.count) && Objects.equals(block, that.block);
  }
