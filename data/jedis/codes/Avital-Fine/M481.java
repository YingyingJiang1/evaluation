  @Override
  public long geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geosearchStore(dest, src, member, width, height, unit));
  }
