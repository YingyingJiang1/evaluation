  public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, byte[] member,
      double radius, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src).add(FROMMEMBER).add(member)
        .add(BYRADIUS).add(radius).add(unit), BuilderFactory.LONG);
  }
