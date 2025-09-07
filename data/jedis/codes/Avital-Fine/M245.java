  public GeoSearchParam byBox(double width, double height, GeoUnit unit){
    this.byBox = true;
    this.width = width;
    this.height = height;
    this.unit = unit;
    return this;
  }
