  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSRangeParams that = (TSRangeParams) o;
    return latest == that.latest && bucketDuration == that.bucketDuration && empty == that.empty &&
        Objects.equals(fromTimestamp, that.fromTimestamp) &&
        Objects.equals(toTimestamp, that.toTimestamp) &&
        Arrays.equals(filterByTimestamps, that.filterByTimestamps) &&
        Arrays.equals(filterByValues, that.filterByValues) &&
        Objects.equals(count, that.count) && Arrays.equals(align, that.align) &&
        aggregationType == that.aggregationType &&
        Arrays.equals(bucketTimestamp, that.bucketTimestamp);
  }
