  private CommandArguments addGeoCoordinateFlatMapArgs(CommandArguments args, Map<?, GeoCoordinate> map) {
    for (Map.Entry<? extends Object, GeoCoordinate> entry : map.entrySet()) {
      GeoCoordinate ord = entry.getValue();
      args.add(ord.getLongitude());
      args.add(ord.getLatitude());
      args.add(entry.getKey());
    }
    return args;
  }
