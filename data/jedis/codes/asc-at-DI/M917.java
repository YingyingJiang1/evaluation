  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TSGetParams that = (TSGetParams) o;
    return latest == that.latest;
  }
