  @Override
  public List<GeoCoordinate> geopos(final byte[] key, final byte[]... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geopos(key, members));
  }
