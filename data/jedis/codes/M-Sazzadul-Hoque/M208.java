  public AbstractTransaction transaction(boolean doMulti) {
    if (provider == null) {
      throw new IllegalStateException("It is not allowed to create Transaction from this " + getClass());
    } else if (provider instanceof MultiClusterPooledConnectionProvider) {
      return new MultiClusterTransaction((MultiClusterPooledConnectionProvider) provider, doMulti, commandObjects);
    } else {
      return new Transaction(provider.getConnection(), doMulti, true, commandObjects);
    }
  }
