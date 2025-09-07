  public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, String member,
      double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCH).key(key).add(FROMMEMBER).add(member)
        .add(BYRADIUS).add(radius).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
