  @Override
  public void close() {
    try {
      sync();
    } finally {
      connections.values().forEach(IOUtils::closeQuietly);
    }
  }
