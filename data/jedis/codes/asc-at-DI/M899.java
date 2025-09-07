  @Override
  public int hashCode() {
    int result = Objects.hashCode(retentionPeriod);
    result = 31 * result + Objects.hashCode(encoding);
    result = 31 * result + Objects.hashCode(chunkSize);
    result = 31 * result + Objects.hashCode(duplicatePolicy);
    result = 31 * result + Objects.hashCode(onDuplicate);
    result = 31 * result + Boolean.hashCode(ignore);
    result = 31 * result + Long.hashCode(ignoreMaxTimediff);
    result = 31 * result + Double.hashCode(ignoreMaxValDiff);
    result = 31 * result + Objects.hashCode(labels);
    return result;
  }
