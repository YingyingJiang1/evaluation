  public final Collection<D> collect(Collection<D> c) {
    while (!isIterationCompleted()) {
      c.addAll(nextBatchList());
    }
    return c;
  }
