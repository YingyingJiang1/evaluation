  private CommandArguments checkAndRoundRobinSearchCommand(SearchCommand sc, String idx1, String idx2) {
    CommandArguments ca = commandArguments(sc);
    if (isRoundRobinSearchCommand()) {
      ca.add(idx1).add(idx2);
    } else {
      ca.key(idx1).key(idx2);
    }
    return ca;
  }
