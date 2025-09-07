  @Override
  public long geosearchStore(String dest, String src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, coord, width, height, unit));
  }
