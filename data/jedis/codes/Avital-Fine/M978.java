  public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, GeoCoordinate coord,
      double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCH).key(key)
        .add(FROMLONLAT).add(coord.getLongitude()).add(coord.getLatitude())
        .add(BYRADIUS).add(radius).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
