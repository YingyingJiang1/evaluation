  protected CommandArguments commandArguments(ProtocolCommand command) {
    CommandArguments comArgs = new CommandArguments(command);
    if (keyPreProcessor != null) comArgs.setKeyArgumentPreProcessor(keyPreProcessor);
    return comArgs;
  }
