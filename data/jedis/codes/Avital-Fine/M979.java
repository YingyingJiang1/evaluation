  public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, String member,
      double width, double height, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCH).key(key).add(FROMMEMBER).add(member)
        .add(BYBOX).add(width).add(height).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
