  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GeoAddParams that = (GeoAddParams) o;
    return nx == that.nx && xx == that.xx && ch == that.ch;
  }
