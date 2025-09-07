  @Override
  public long renamenx(final String oldkey, final String newkey) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.renamenx(oldkey, newkey));
  }
