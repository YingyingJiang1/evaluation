  @Override
  public long geosearchStore(String dest, String src, String member, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, member, radius, unit));
  }
