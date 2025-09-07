  @Override
  public final <T> T executeCommand(CommandObject<T> commandObject) {
    try (Connection connection = provider.getConnection(commandObject.getArguments())) {
      return connection.executeCommand(commandObject);
    }
  }
