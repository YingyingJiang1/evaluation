  @Override
  protected ClusterCommandArguments commandArguments(ProtocolCommand command) {
    ClusterCommandArguments comArgs = new ClusterCommandArguments(command);
    if (keyPreProcessor != null) comArgs.setKeyArgumentPreProcessor(keyPreProcessor);
    return comArgs;
  }
