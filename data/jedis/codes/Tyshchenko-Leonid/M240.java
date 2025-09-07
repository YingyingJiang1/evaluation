  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XPendingParams that = (XPendingParams) o;
    return Objects.equals(idle, that.idle) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(count, that.count) && Objects.equals(consumer, that.consumer);
  }
