  @Override
  public List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, coord, width, height, unit));
  }
