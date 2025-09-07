  @Override
  public void disconnect() {
    super.disconnect();
    cache.flush();
  }
