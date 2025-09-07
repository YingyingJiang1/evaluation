  @Override
  public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearch(key, member, width, height, unit));
  }
