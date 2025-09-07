  private void clear() {
    if (inMulti) {
      discard();
    } else if (inWatch) {
      unwatch();
    }
  }
