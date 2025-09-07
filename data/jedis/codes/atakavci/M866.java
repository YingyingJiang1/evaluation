  protected void sendAndFlushCommand(Command command, Object... args) {
    if (client == null) {
      throw new JedisException(getClass() + " is not connected to a Connection.");
    }
    CommandArguments cargs = new CommandArguments(command).addObjects(args);

    Token newToken = pendingTokenRef.getAndSet(PLACEHOLDER_TOKEN);

    // lets send the command without locking !!IF!! we know that pendingTokenRef is null replaced with PLACEHOLDER_TOKEN and no re-auth will go into action
    // !!ELSE!! we are locking since we already know a re-auth is still in progress in another thread and we need to wait for it to complete, we do nothing but wait on it!
    if (newToken != null) {
      commandSync.lock();
    }
    try {
      client.sendCommand(cargs);
      client.flush();
    } finally {
      Token newerToken = pendingTokenRef.getAndSet(null);
      // lets check if a newer token received since the beginning of this sendAndFlushCommand call
      if (newerToken != null && newerToken != PLACEHOLDER_TOKEN) {
        safeReAuthenticate(newerToken);
      }
      if (newToken != null) {
        commandSync.unlock();
      }
    }
  }
