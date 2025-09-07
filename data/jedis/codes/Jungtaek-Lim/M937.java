  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o == this) return true;
    if (!(o instanceof GeoCoordinate)) return false;

    GeoCoordinate that = (GeoCoordinate) o;

    if (Double.compare(that.longitude, longitude) != 0) return false;
    return Double.compare(that.latitude, latitude) == 0;
  }
