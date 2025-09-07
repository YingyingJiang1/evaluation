  @Override
  public int hashCode() {
    int result = Objects.hashCode(fromTimestamp);
    result = 31 * result + Objects.hashCode(toTimestamp);
    result = 31 * result + Boolean.hashCode(latest);
    result = 31 * result + Arrays.hashCode(filterByTimestamps);
    result = 31 * result + Arrays.hashCode(filterByValues);
    result = 31 * result + Boolean.hashCode(withLabels);
    result = 31 * result + Arrays.hashCode(selectedLabels);
    result = 31 * result + Objects.hashCode(count);
    result = 31 * result + Arrays.hashCode(align);
    result = 31 * result + Objects.hashCode(aggregationType);
    result = 31 * result + Long.hashCode(bucketDuration);
    result = 31 * result + Arrays.hashCode(bucketTimestamp);
    result = 31 * result + Boolean.hashCode(empty);
    result = 31 * result + Arrays.hashCode(filters);
    result = 31 * result + Objects.hashCode(groupByLabel);
    result = 31 * result + Objects.hashCode(groupByReduce);
    return result;
  }
