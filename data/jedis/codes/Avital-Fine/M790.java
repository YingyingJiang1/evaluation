  @Override
  public List<GeoRadiusResponse> geosearch(String key, String member, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, member, radius, unit));
  }
