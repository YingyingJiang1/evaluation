  public GeoRadiusStoreParam storeDist(String key) {
    if (key != null) {
      this.storeDist = true;
      this.key = key;
    }
    return this;
  }
