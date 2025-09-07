  @Override
  public long geoadd(final byte[] key, final Map<byte[], GeoCoordinate> memberCoordinateMap) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geoadd(key, memberCoordinateMap));
  }
