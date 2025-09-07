  public final CommandObject<Map<String, Map<String, Double>>> ftSpellCheck(String index, String query,
      FTSpellCheckParams spellCheckParams) {
    return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchCommand.SPELLCHECK, index).add(query)
        .addParams(spellCheckParams.dialectOptional(searchDialect.get())), SearchBuilderFactory.SEARCH_SPELLCHECK_RESPONSE);
  }
