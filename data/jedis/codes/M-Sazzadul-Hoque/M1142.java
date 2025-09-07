  private boolean handleConnectionProblem(int attemptsLeft, int consecutiveConnectionFailures, Instant doneDeadline) {

    if (consecutiveConnectionFailures < 2) {
      return false;
    }

    sleep(getBackoffSleepMillis(attemptsLeft, doneDeadline));
    return true;
  }
