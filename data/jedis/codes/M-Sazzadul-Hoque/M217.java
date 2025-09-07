  public ZRangeParams limit(int offset, int count) {
    this.limit = true;
    this.offset = offset;
    this.count = count;
    return this;
  }
