  @Override
  public long waitReplicas(final int replicas, final long timeout) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(WAIT, toByteArray(replicas), toByteArray(timeout));
    return connection.getIntegerReply();
  }
