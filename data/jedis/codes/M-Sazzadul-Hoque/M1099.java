  @Override
  public final String unwatch() {
    appendCommand(new CommandObject<>(new CommandArguments(UNWATCH), NO_OP_BUILDER));
    extraCommandCount.incrementAndGet();
    inWatch = false;
    return null;
  }
