  @Override
  public long geosearchStore(String dest, String src, String member, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, member, width, height, unit));
  }
