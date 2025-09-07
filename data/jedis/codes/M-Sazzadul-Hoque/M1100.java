  @Override
  protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
    CommandArguments args = commandObject.getArguments();
    Response<T> response = new Response<>(commandObject.getBuilder());
    commands.add(KeyValue.of(args, response));
    return response;
  }
