  @Override
  protected CommandArguments nextCommandArguments(SearchResult lastReply) {
    batchStart += batchSize;
    return args.apply(batchStart);
  }
