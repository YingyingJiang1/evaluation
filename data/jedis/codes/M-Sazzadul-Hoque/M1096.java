  @Override
  public final void multi() {
    appendCommand(new CommandObject<>(new CommandArguments(MULTI), NO_OP_BUILDER));
    extraCommandCount.incrementAndGet();
    inMulti = true;
  }
