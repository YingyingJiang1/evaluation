  @Override
  protected CommandArguments nextCommandArguments(AggregationResult lastReply) {
    return new CommandArguments(SearchProtocol.SearchCommand.CURSOR).add(SearchProtocol.SearchKeyword.READ)
        .add(indexName).add(lastReply.getCursorId());
  }
