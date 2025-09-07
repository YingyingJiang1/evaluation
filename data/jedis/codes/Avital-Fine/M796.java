  @Override
  public long geosearchStore(String dest, String src, GeoCoordinate coord, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, coord, radius, unit));
  }
