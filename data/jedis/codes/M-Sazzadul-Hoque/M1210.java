  private void reset() {
    for (ConnectionPool pool : resources.values()) {
      try {
        if (pool != null) {
          pool.destroy();
        }
      } catch (RuntimeException e) {
        // pass
      }
    }
    resources.clear();
    nodes.clear();
  }
