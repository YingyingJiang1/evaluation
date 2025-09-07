  public final CommandObject<Long> georadiusStore(String key, double longitude, double latitude,
      double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return new CommandObject<>(commandArguments(GEORADIUS).key(key).add(longitude).add(latitude)
        .add(radius).add(unit).addParams(param).addParams(storeParam), BuilderFactory.LONG);
  }
