  public final CommandObject<String> ftAlter(String indexName, Schema schema) {
    CommandArguments args = checkAndRoundRobinSearchCommand(SearchCommand.ALTER, indexName)
        .add(SearchKeyword.SCHEMA).add(SearchKeyword.ADD);
    schema.fields.forEach(field -> args.addParams(field));
    return new CommandObject<>(args, BuilderFactory.STRING);
  }
