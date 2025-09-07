  private <T> T checkAndBroadcastCommand(CommandObject<T> commandObject) {
    boolean broadcast = true;

    if (broadcastAndRoundRobinConfig == null) {
    } else if (commandObject.getArguments().getCommand() instanceof SearchProtocol.SearchCommand
        && broadcastAndRoundRobinConfig
            .getRediSearchModeInCluster() == JedisBroadcastAndRoundRobinConfig.RediSearchMode.LIGHT) {
      broadcast = false;
    }

    return broadcast ? broadcastCommand(commandObject) : executeCommand(commandObject);
  }
