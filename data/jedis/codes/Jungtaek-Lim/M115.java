  public void proceed(Connection client) {
    this.client = client;
    this.client.setTimeoutInfinite();
    do {
      String command = client.getBulkReply();
      onCommand(command);
    } while (client.isConnected());
  }
