  @Override
  public String watch(final byte[]... keys) {
    String status = connection.executeCommand(commandObjects.watch(keys));
    inWatch = true;
    return status;
  }
