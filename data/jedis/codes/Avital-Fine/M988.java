  public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, GeoCoordinate coord,
      double width, double height, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCH).key(key)
        .add(FROMLONLAT).add(coord.getLongitude()).add(coord.getLatitude())
        .add(BYBOX).add(width).add(height).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
