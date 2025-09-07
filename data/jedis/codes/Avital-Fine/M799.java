  @Override
  public long geosearchStore(String dest, String src, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, params));
  }
