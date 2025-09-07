  @Override
  public final String discard() {
    if (!inMulti) {
      throw new IllegalStateException("DISCARD without MULTI");
    }

    try (Connection connection = failoverProvider.getConnection()) {

      commands.forEach((command) -> connection.sendCommand(command.getKey()));
      // following connection.getMany(int) flushes anyway, so no flush here.

      // ignore QUEUED (or ERROR)
      connection.getMany(commands.size());

      connection.sendCommand(DISCARD);

      return connection.getStatusCodeReply();
    } finally {
      inMulti = false;
      inWatch = false;
    }
  }
