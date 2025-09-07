  @Override
  public long georadiusByMemberStore(final byte[] key, final byte[] member, final double radius,
      final GeoUnit unit, final GeoRadiusParam param, final GeoRadiusStoreParam storeParam) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam));
  }
