  @Override
  public Response<String> swapDB(final int index1, final int index2) {
    return appendCommand(new CommandObject<>(commandObjects.commandArguments(Protocol.Command.SWAPDB)
        .add(index1).add(index2), BuilderFactory.STRING));
  }
