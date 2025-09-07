  @Override
  public long geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, member, radius, unit));
  }
