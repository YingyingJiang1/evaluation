  public final CommandObject<Long> geosearchStore(String dest, String src, String member,
      double width, double height, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src).add(FROMMEMBER).add(member)
        .add(BYBOX).add(width).add(height).add(unit), BuilderFactory.LONG);
  }
