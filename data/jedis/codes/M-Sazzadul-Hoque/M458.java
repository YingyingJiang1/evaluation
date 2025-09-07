  @Override
  public long geoadd(final byte[] key, final double longitude, final double latitude,
      final byte[] member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geoadd(key, longitude, latitude, member));
  }
