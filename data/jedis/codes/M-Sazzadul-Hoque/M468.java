  @Override
  public long georadiusStore(final byte[] key, final double longitude, final double latitude,
      final double radius, final GeoUnit unit, final GeoRadiusParam param,
      final GeoRadiusStoreParam storeParam) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam));
  }
