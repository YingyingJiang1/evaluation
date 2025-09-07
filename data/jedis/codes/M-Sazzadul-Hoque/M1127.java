  @Override
  public Response<Long> move(final byte[] key, final int dbIndex) {
    return appendCommand(new CommandObject<>(commandObjects.commandArguments(Protocol.Command.MOVE)
        .key(key).add(dbIndex), BuilderFactory.LONG));
  }
