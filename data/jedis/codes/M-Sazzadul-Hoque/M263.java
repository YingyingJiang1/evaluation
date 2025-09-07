  public void resetState() {
    if (isConnected()) {
      if (transaction != null) {
        transaction.close();
      }

      if (pipeline != null) {
        pipeline.close();
      }

//      connection.resetState();
      if (isInWatch) {
        connection.sendCommand(UNWATCH);
        connection.getStatusCodeReply();
        isInWatch = false;
      }
    }

    transaction = null;
    pipeline = null;
  }
