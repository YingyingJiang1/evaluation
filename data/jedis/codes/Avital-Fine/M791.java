  @Override
  public List<GeoRadiusResponse> geosearch(String key, GeoCoordinate coord, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, coord, radius, unit));
  }
