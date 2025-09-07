  @Override
  public String migrate(final String host, final int port, final int destinationDB,
      final int timeout, final MigrateParams params, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.migrate(host, port, destinationDB, timeout, params, keys));
  }
