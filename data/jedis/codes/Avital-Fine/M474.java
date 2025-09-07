  @Override
  public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, member, radius, unit));
  }
