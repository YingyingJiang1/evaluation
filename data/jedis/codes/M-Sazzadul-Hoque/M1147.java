  private boolean handleConnectionProblem(int attemptsLeft, int consecutiveConnectionFailures, Instant doneDeadline) {
    if (this.maxAttempts < 3) {
      // Since we only renew the slots cache after two consecutive connection
      // failures (see consecutiveConnectionFailures above), we need to special
      // case the situation where we max out after two or fewer attempts.
      // Otherwise, on two or fewer max attempts, the slots cache would never be
      // renewed.
      if (attemptsLeft == 0) {
        provider.renewSlotCache();
        return true;
      }
      return false;
    }

    if (consecutiveConnectionFailures < 2) {
      return false;
    }

    sleep(getBackoffSleepMillis(attemptsLeft, doneDeadline));
    //We need this because if node is not reachable anymore - we need to finally initiate slots
    //renewing, or we can stuck with cluster state without one node in opposite case.
    //TODO make tracking of successful/unsuccessful operations for node - do renewing only
    //if there were no successful responses from this node last few seconds
    provider.renewSlotCache();
    return true;
  }
