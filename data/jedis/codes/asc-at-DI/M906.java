  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSMGetParams that = (TSMGetParams) o;
    return latest == that.latest && withLabels == that.withLabels &&
        Arrays.equals(selectedLabels, that.selectedLabels);
  }
