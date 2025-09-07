  public GeoRadiusStoreParam store(String key) {
    if (key != null) {
      this.store = true;
      this.key = key;
    }
    return this;
  }
