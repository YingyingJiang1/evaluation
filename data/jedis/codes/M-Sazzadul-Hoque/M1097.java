  @Override
  public final String watch(String... keys) {
    appendCommand(commandObjects.watch(keys));
    extraCommandCount.incrementAndGet();
    inWatch = true;
    return null;
  }
