  public GeoSearchParam fromLonLat(GeoCoordinate coord) {
    this.fromLonLat = true;
    this.coord = coord;
    return this;
  }
