  @Override
  public List<GeoRadiusResponse> georadiusByMember(final byte[] key, final byte[] member,
      final double radius, final GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadiusByMember(key, member, radius, unit));
  }
