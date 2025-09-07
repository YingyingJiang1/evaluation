  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSArithByParams<?> that = (TSArithByParams<?>) o;
    return ignore == that.ignore && ignoreMaxTimediff == that.ignoreMaxTimediff &&
        Double.compare(ignoreMaxValDiff, that.ignoreMaxValDiff) == 0 &&
        Objects.equals(timestamp, that.timestamp) &&
        Objects.equals(retentionPeriod, that.retentionPeriod) &&
        encoding == that.encoding && Objects.equals(chunkSize, that.chunkSize) &&
        duplicatePolicy == that.duplicatePolicy && Objects.equals(labels, that.labels);
  }
