  @Override
  public long geoadd(final byte[] key, final GeoAddParams params, final Map<byte[], GeoCoordinate> memberCoordinateMap) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geoadd(key, params, memberCoordinateMap));
  }
