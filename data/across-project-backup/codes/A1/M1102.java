  @Override
  public final List<Object> exec() {
    if (!inMulti) {
      throw new IllegalStateException("EXEC without MULTI");
    }

    try (Connection connection = failoverProvider.getConnection()) {

      commands.forEach((command) -> connection.sendCommand(command.getKey()));
      // following connection.getMany(int) flushes anyway, so no flush here.

      // ignore QUEUED (or ERROR)
      connection.getMany(commands.size());

      // remove extra response builders
      for (int idx = 0; idx < extraCommandCount.get(); ++idx) {
        commands.poll();
      }

      connection.sendCommand(EXEC);

      List<Object> unformatted = connection.getObjectMultiBulkReply();
      if (unformatted == null) {
        commands.clear();
        return null;
      }

      List<Object> formatted = new ArrayList<>(unformatted.size() - extraCommandCount.get());
      for (Object rawReply: unformatted) {
        try {
          Response<?> response = commands.poll().getValue();
          response.set(rawReply);
          formatted.add(response.get());
        } catch (JedisDataException e) {
          formatted.add(e);
        }
      }
      return formatted;

    } finally {
      inMulti = false;
      inWatch = false;
    }
  }
