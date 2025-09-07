  @Override
  public String sentinelSet(String masterName, Map<String, String> parameterMap) {
    CommandArguments args = new CommandArguments(SENTINEL).add(SentinelKeyword.SET).add(masterName);
    parameterMap.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
    connection.sendCommand(args);
    return connection.getStatusCodeReply();
  }
