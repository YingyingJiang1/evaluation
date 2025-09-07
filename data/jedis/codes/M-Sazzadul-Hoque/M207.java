  public PipelineBase pipelined() {
    if (provider == null) {
      throw new IllegalStateException("It is not allowed to create Pipeline from this " + getClass());
    } else if (provider instanceof MultiClusterPooledConnectionProvider) {
      return new MultiClusterPipeline((MultiClusterPooledConnectionProvider) provider, commandObjects);
    } else {
      return new Pipeline(provider.getConnection(), true, commandObjects);
    }
  }
