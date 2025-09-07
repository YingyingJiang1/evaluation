  @Override
  public long geosearchStoreStoreDist(String dest, String src, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStoreStoreDist(dest, src, params));
  }
