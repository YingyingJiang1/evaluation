  public <T> T executeCommand(final CommandObject<T> commandObject) {
    final CommandArguments args = commandObject.getArguments();
    sendCommand(args);
    if (!args.isBlocking()) {
      return commandObject.getBuilder().build(getOne());
    } else {
      try {
        setTimeoutInfinite();
        return commandObject.getBuilder().build(getOne());
      } finally {
        rollbackTimeout();
      }
    }
  }
