  private boolean isRoundRobinSearchCommand() {
    if (broadcastAndRoundRobinConfig == null) {
      return true;
    } else if (broadcastAndRoundRobinConfig.getRediSearchModeInCluster() == JedisBroadcastAndRoundRobinConfig.RediSearchMode.LIGHT) {
      return false;
    }
    return true;
  }
