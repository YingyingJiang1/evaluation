  public void destroy() {
    try {
      super.close();
    } catch (RuntimeException e) {
      throw new JedisException("Could not destroy the pool", e);
    }
  }
