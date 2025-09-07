  public long latencyReset(LatencyEvent... events) {
    checkIsInMultiOrPipeline();
    CommandArguments arguments = new CommandArguments(LATENCY).add(Keyword.RESET);
    Arrays.stream(events).forEach(arguments::add);
    connection.sendCommand(arguments);
    return connection.getIntegerReply();
  }
