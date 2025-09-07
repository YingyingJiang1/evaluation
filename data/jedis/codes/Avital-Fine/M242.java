  public GeoSearchParam fromLonLat(double longitude, double latitude) {
    this.fromLonLat = true;
    this.coord = new GeoCoordinate(longitude, latitude);
    return this;
  }
