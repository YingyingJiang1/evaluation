  @Override
  public final <T> T executeCommand(CommandObject<T> commandObject) {

    Instant deadline = Instant.now().plus(maxTotalRetriesDuration);

    int consecutiveConnectionFailures = 0;
    JedisException lastException = null;
    for (int attemptsLeft = this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
      Connection connection = null;
      try {
        connection = provider.getConnection(commandObject.getArguments());

        return execute(connection, commandObject);

      } catch (JedisConnectionException jce) {
        lastException = jce;
        ++consecutiveConnectionFailures;
        log.debug("Failed connecting to Redis: {}", connection, jce);
        // "- 1" because we just did one, but the attemptsLeft counter hasn't been decremented yet
        boolean reset = handleConnectionProblem(attemptsLeft - 1, consecutiveConnectionFailures, deadline);
        if (reset) {
          consecutiveConnectionFailures = 0;
        }
      } finally {
        if (connection != null) {
          connection.close();
        }
      }
      if (Instant.now().isAfter(deadline)) {
        throw new JedisException("Retry deadline exceeded.");
      }
    }

    JedisException maxAttemptsException = new JedisException("No more attempts left.");
    maxAttemptsException.addSuppressed(lastException);
    throw maxAttemptsException;
  }
