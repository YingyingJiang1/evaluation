  public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord,
      double width, double height, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src)
        .add(FROMLONLAT).add(coord.getLongitude()).add(coord.getLatitude())
        .add(BYBOX).add(width).add(height).add(unit), BuilderFactory.LONG);
  }
