  @Override
  public final void close() {
    try {
      clear();
    } finally {
      if (closeConnection) {
        connection.close();
      }
    }
  }
