  public CommandArguments add(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("null is not a valid argument.");
    } else if (arg instanceof Rawable) {
      args.add((Rawable) arg);
    } else if (arg instanceof byte[]) {
      args.add(RawableFactory.from((byte[]) arg));
    } else if (arg instanceof Boolean) {
      args.add(RawableFactory.from((Boolean) arg));
    } else if (arg instanceof Integer) {
      args.add(RawableFactory.from((Integer) arg));
    } else if (arg instanceof Long) {
      args.add(RawableFactory.from((Long) arg));
    } else if (arg instanceof Double) {
      args.add(RawableFactory.from((Double) arg));
    } else if (arg instanceof float[]) {
      args.add(RawableFactory.from(RediSearchUtil.toByteArray((float[]) arg)));
    } else if (arg instanceof String) {
      args.add(RawableFactory.from((String) arg));
    } else if (arg instanceof GeoCoordinate) {
      GeoCoordinate geo = (GeoCoordinate) arg;
      args.add(RawableFactory.from(geo.getLongitude() + "," + geo.getLatitude()));
    } else {
      args.add(RawableFactory.from(String.valueOf(arg)));
    }
    return this;
  }
