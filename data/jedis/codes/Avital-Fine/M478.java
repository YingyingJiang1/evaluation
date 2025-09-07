  @Override
  public List<GeoRadiusResponse> geosearch(byte[] key, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, params));
  }
