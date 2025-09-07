  public Map<String, LatencyLatestInfo> latencyLatest() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(LATENCY, LATEST);
    return BuilderFactory.LATENCY_LATEST_RESPONSE.build(connection.getOne());
  }
