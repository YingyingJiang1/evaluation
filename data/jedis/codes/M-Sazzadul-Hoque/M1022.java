  private CommandArguments checkAndRoundRobinSearchCommand(SearchCommand sc, String idx) {
    CommandArguments ca = commandArguments(sc);
    if (isRoundRobinSearchCommand()) {
      ca.add(idx);
    } else {
      ca.key(idx);
    }
    return ca;
  }
