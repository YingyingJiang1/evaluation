  public List<LatencyHistoryInfo> latencyHistory(LatencyEvent event) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(new CommandArguments(LATENCY).add(HISTORY).add(event));
    return BuilderFactory.LATENCY_HISTORY_RESPONSE.build(connection.getOne());
  }
