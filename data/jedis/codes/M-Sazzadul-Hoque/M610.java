  @Override
  public String rpoplpush(final String srckey, final String dstkey) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rpoplpush(srckey, dstkey));
  }
