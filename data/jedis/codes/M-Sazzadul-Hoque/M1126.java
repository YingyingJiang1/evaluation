  @Override
  public Response<Long> move(String key, int dbIndex) {
    return appendCommand(new CommandObject<>(commandObjects.commandArguments(Protocol.Command.MOVE)
        .key(key).add(dbIndex), BuilderFactory.LONG));
  }
