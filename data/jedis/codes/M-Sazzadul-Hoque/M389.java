  @Override
  public List<byte[]> objectHelpBinary() {
    connection.sendCommand(OBJECT, HELP);
    return connection.getBinaryMultiBulkReply();
  }
