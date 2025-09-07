  public GeoSearchParam byRadius(double radius, GeoUnit unit){
    this.byRadius = true;
    this.radius = radius;
    this.unit = unit;
    return this;
  }
