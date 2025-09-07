  @Override
  public Double geodist(final byte[] key, final byte[] member1, final byte[] member2,
      final GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geodist(key, member1, member2, unit));
  }
