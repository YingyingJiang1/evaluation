  @Override
  public final String watch(byte[]... keys) {
    appendCommand(commandObjects.watch(keys));
    extraCommandCount.incrementAndGet();
    inWatch = true;
    return null;
  }
