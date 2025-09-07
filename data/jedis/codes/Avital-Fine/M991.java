  public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, byte[] member,
      double width, double height, GeoUnit unit) {
    return new CommandObject<>(commandArguments(GEOSEARCHSTORE).key(dest).add(src).add(FROMMEMBER).add(member)
        .add(BYBOX).add(width).add(height).add(unit), BuilderFactory.LONG);
  }
