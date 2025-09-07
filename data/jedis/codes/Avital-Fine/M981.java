  public final CommandObject<Long> geosearchStore(String dest, String src, String member,
      double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src).add(FROMMEMBER).add(member)
        .add(BYRADIUS).add(radius).add(unit), BuilderFactory.LONG);
  }
