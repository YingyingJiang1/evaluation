  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSMRangeParams that = (TSMRangeParams) o;
    return latest == that.latest && withLabels == that.withLabels &&
        bucketDuration == that.bucketDuration && empty == that.empty &&
        Objects.equals(fromTimestamp, that.fromTimestamp) &&
        Objects.equals(toTimestamp, that.toTimestamp) &&
        Arrays.equals(filterByTimestamps, that.filterByTimestamps) &&
        Arrays.equals(filterByValues, that.filterByValues) &&
        Arrays.equals(selectedLabels, that.selectedLabels) &&
        Objects.equals(count, that.count) && Arrays.equals(align, that.align) &&
        aggregationType == that.aggregationType &&
        Arrays.equals(bucketTimestamp, that.bucketTimestamp) &&
        Arrays.equals(filters, that.filters) &&
        Objects.equals(groupByLabel, that.groupByLabel) &&
        Objects.equals(groupByReduce, that.groupByReduce);
  }
