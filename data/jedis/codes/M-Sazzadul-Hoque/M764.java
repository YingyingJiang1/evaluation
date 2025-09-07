  @Override
  public String migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.migrate(host, port, timeout, params, keys));
  }
