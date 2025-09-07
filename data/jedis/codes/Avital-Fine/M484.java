  @Override
  public long geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStoreStoreDist(dest, src, params));
  }
