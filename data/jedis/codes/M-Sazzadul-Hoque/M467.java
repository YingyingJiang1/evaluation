  @Override
  public List<GeoRadiusResponse> georadius(final byte[] key, final double longitude,
      final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadius(key, longitude, latitude, radius, unit, param));
  }
