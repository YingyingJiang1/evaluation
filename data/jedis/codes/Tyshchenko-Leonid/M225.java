  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XReadGroupParams that = (XReadGroupParams) o;
    return noack == that.noack && Objects.equals(count, that.count) && Objects.equals(block, that.block);
  }
