  @Override
  public long geosearchStore(byte[] dest, byte[] src, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, params));
  }
