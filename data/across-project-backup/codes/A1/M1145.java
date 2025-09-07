  @Override
  public final <T> T broadcastCommand(CommandObject<T> commandObject) {
    Map<String, ConnectionPool> connectionMap = provider.getConnectionMap();

    boolean isErrored = false;
    T reply = null;
    JedisBroadcastException bcastError = new JedisBroadcastException();
    for (Map.Entry<String, ConnectionPool> entry : connectionMap.entrySet()) {
      HostAndPort node = HostAndPort.from(entry.getKey());
      ConnectionPool pool = entry.getValue();
      try (Connection connection = pool.getResource()) {
        T aReply = execute(connection, commandObject);
        bcastError.addReply(node, aReply);
        if (isErrored) { // already errored
        } else if (reply == null) {
          reply = aReply; // ok
        } else if (reply.equals(aReply)) {
          // ok
        } else {
          isErrored = true;
          reply = null;
        }
      } catch (Exception anError) {
        bcastError.addReply(node, anError);
        isErrored = true;
      }
    }
    if (isErrored) {
      throw bcastError;
    }
    return reply;
  }
