  public final CommandObject<List<GeoRadiusResponse>> georadiusReadonly(byte[] key,
      double longitude, double latitude, double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEORADIUS_RO).key(key).add(longitude).add(latitude)
        .add(radius).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
