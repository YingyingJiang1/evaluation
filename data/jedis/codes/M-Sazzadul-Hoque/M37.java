  @Override
  public String watch(final String... keys) {
    String status = connection.executeCommand(commandObjects.watch(keys));
    inWatch = true;
    return status;
  }
