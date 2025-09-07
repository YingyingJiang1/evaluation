  @Override
  public List<GeoRadiusResponse> georadiusReadonly(final byte[] key, final double longitude,
      final double latitude, final double radius, final GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
  }
