  @Override
  public String rename(final String oldkey, final String newkey) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rename(oldkey, newkey));
  }
