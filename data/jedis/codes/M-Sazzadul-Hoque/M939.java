  @Override
  protected ShardedCommandArguments commandArguments(ProtocolCommand command) {
    ShardedCommandArguments comArgs = new ShardedCommandArguments(algo, tagPattern, command);
    if (keyPreProcessor != null) comArgs.setKeyArgumentPreProcessor(keyPreProcessor);
    return comArgs;
  }
