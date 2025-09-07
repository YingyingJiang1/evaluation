  @Override
  public long expireTime(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand((commandObjects.expireTime(key)));
  }
