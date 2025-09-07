  public final CommandObject<String> ftAlter(String indexName, Iterable<SchemaField> schemaFields) {
    CommandArguments args = checkAndRoundRobinSearchCommand(SearchCommand.ALTER, indexName)
        .add(SearchKeyword.SCHEMA).add(SearchKeyword.ADD);
    schemaFields.forEach(field -> args.addParams(field));
    return new CommandObject<>(args, BuilderFactory.STRING);
  }
