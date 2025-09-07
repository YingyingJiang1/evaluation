  @Override
  public List<byte[]> geohash(final byte[] key, final byte[]... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.geohash(key, members));
  }
