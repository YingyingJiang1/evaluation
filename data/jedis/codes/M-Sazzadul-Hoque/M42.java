  @Deprecated // TODO: private
  public final void clear() {
    if (broken) {
      return;
    }
    if (inMulti) {
      discard();
    } else if (inWatch) {
      unwatch();
    }
  }
