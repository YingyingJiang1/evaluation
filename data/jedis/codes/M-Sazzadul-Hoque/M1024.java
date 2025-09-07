  public final CommandObject<String> ftCreate(String indexName, IndexOptions indexOptions, Schema schema) {
    CommandArguments args = checkAndRoundRobinSearchCommand(SearchCommand.CREATE, indexName)
        .addParams(indexOptions).add(SearchKeyword.SCHEMA);
    schema.fields.forEach(field -> args.addParams(field));
    return new CommandObject<>(args, BuilderFactory.STRING);
  }
