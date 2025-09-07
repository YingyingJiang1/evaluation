  public final B nextBatch() {
    if (roundRobinCompleted) {
      throw new NoSuchElementException();
    }

    CommandArguments args;
    if (iterationCompleted) {
      connection = connections.poll();
      args = initCommandArguments();
    } else {
      args = nextCommandArguments(lastReply);
    }

    Object rawReply;
    if (connection.getValue() instanceof Connection) {
      rawReply = ((Connection) connection.getValue()).executeCommand(args);
    } else if (connection.getValue() instanceof Pool) {
      try (Connection c = ((Pool<Connection>) connection.getValue()).getResource()) {
        rawReply = c.executeCommand(args);
      }
    } else {
      throw new IllegalArgumentException(connection.getValue().getClass() + "is not supported.");
    }

    lastReply = builder.build(rawReply);
    iterationCompleted = isNodeCompleted(lastReply);
    if (iterationCompleted) {
      if (connections.isEmpty()) {
        roundRobinCompleted = true;
      }
    }
    return lastReply;
  }
