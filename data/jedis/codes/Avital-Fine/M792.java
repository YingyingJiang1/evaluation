  @Override
  public List<GeoRadiusResponse> geosearch(String key, String member, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, member, width, height, unit));
  }
