  public final CommandObject<String> ftCreate(String indexName, FTCreateParams createParams,
      Iterable<SchemaField> schemaFields) {
    CommandArguments args = checkAndRoundRobinSearchCommand(SearchCommand.CREATE, indexName)
        .addParams(createParams).add(SearchKeyword.SCHEMA);
    schemaFields.forEach(field -> args.addParams(field));
    return new CommandObject<>(args, BuilderFactory.STRING);
  }
