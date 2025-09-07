  public final CommandObject<Long> geosearchStore(String dest, String src, GeoCoordinate coord,
      double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src).add(FROMLONLAT).add(coord.getLongitude())
        .add(coord.getLatitude()).add(BYRADIUS).add(radius).add(unit), BuilderFactory.LONG);
  }
