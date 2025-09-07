  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZAddParams that = (ZAddParams) o;
    return change == that.change && existence == that.existence && comparison == that.comparison;
  }
