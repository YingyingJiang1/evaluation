  @Override
  protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
    connection.sendCommand(commandObject.getArguments());
    String status = connection.getStatusCodeReply();
    if (!QUEUED_STR.equals(status)) {
      throw new JedisException(status);
    }
    Response<T> response = new Response<>(commandObject.getBuilder());
    pipelinedResponses.add(response);
    return response;
  }
