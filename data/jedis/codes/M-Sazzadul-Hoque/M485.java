  @Override
  public List<GeoRadiusResponse> georadiusByMemberReadonly(final byte[] key, final byte[] member,
      final double radius, final GeoUnit unit, final GeoRadiusParam param) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
  }
