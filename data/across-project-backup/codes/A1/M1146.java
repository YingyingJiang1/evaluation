  private <T> T doExecuteCommand(CommandObject<T> commandObject, boolean toReplica) {
    Instant deadline = Instant.now().plus(maxTotalRetriesDuration);

    JedisRedirectionException redirect = null;
    int consecutiveConnectionFailures = 0;
    Exception lastException = null;
    for (int attemptsLeft = this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
      Connection connection = null;
      try {
        if (redirect != null) {
          connection = provider.getConnection(redirect.getTargetNode());
          if (redirect instanceof JedisAskDataException) {
            // TODO: Pipeline asking with the original command to make it faster....
            connection.executeCommand(Protocol.Command.ASKING);
          }
        } else {
          connection = toReplica ? provider.getReplicaConnection(commandObject.getArguments())
              : provider.getConnection(commandObject.getArguments());
        }

        return execute(connection, commandObject);

      } catch (JedisClusterOperationException jnrcne) {
        throw jnrcne;
      } catch (JedisConnectionException jce) {
        lastException = jce;
        ++consecutiveConnectionFailures;
        log.debug("Failed connecting to Redis: {}", connection, jce);
        // "- 1" because we just did one, but the attemptsLeft counter hasn't been decremented yet
        boolean reset = handleConnectionProblem(attemptsLeft - 1, consecutiveConnectionFailures, deadline);
        if (reset) {
          consecutiveConnectionFailures = 0;
          redirect = null;
        }
      } catch (JedisRedirectionException jre) {
        // avoid updating lastException if it is a connection exception
        if (lastException == null || lastException instanceof JedisRedirectionException) {
          lastException = jre;
        }
        log.debug("Redirected by server to {}", jre.getTargetNode());
        consecutiveConnectionFailures = 0;
        redirect = jre;
        // if MOVED redirection occurred,
        if (jre instanceof JedisMovedDataException) {
          // it rebuilds cluster's slot cache recommended by Redis cluster specification
          provider.renewSlotCache(connection);
        }
      } finally {
        IOUtils.closeQuietly(connection);
      }
      if (Instant.now().isAfter(deadline)) {
        throw new JedisClusterOperationException("Cluster retry deadline exceeded.", lastException);
      }
    }

    JedisClusterOperationException maxAttemptsException
        = new JedisClusterOperationException("No more cluster attempts left.");
    maxAttemptsException.addSuppressed(lastException);
    throw maxAttemptsException;
  }
