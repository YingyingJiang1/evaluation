  public final CommandObject<List<GeoRadiusResponse>> georadiusByMember(String key,
      String member, double radius, GeoUnit unit, GeoRadiusParam param) {
    return new CommandObject<>(commandArguments(GEORADIUSBYMEMBER).key(key).add(member)
        .add(radius).add(unit).addParams(param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
  }
