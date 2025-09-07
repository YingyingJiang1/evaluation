  @Override
  public Map<String, Map<String, Double>> ftSpellCheck(String index, String query,
      FTSpellCheckParams spellCheckParams) {
    return executeCommand(commandObjects.ftSpellCheck(index, query, spellCheckParams));
  }
