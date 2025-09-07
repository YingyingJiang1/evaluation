  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSAddParams that = (TSAddParams) o;
    return ignore == that.ignore && ignoreMaxTimediff == that.ignoreMaxTimediff &&
        Double.compare(ignoreMaxValDiff, that.ignoreMaxValDiff) == 0 &&
        Objects.equals(retentionPeriod, that.retentionPeriod) &&
        encoding == that.encoding && Objects.equals(chunkSize, that.chunkSize) &&
        duplicatePolicy == that.duplicatePolicy && onDuplicate == that.onDuplicate &&
        Objects.equals(labels, that.labels);
  }
