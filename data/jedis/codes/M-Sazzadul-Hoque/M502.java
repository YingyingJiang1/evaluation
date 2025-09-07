  @Deprecated
  @Override
  public List<Object> xread(XReadParams xReadParams, Entry<byte[], byte[]>... streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xread(xReadParams, streams));
  }
