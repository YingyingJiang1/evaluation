  @Override
  protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
    connection.sendCommand(commandObject.getArguments());
    // processAppendStatus(); // do nothing
    Response<T> response = new Response<>(commandObject.getBuilder());
    pipelinedResponses.add(response);
    return response;
  }
