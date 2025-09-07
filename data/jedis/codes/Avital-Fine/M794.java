  @Override
  public List<GeoRadiusResponse> geosearch(String key, GeoSearchParam params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, params));
  }
