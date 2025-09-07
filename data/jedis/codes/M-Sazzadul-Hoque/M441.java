  @Override
  public String migrate(final String host, final int port, final byte[] key,
      final int destinationDb, final int timeout) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.migrate(host, port, key, destinationDb, timeout));
  }
