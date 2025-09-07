  @Override
  public List<String> sentinelGetMasterAddrByName(String masterName) {
    connection.sendCommand(SENTINEL, GET_MASTER_ADDR_BY_NAME.getRaw(), encode(masterName));
    return connection.getMultiBulkReply();
  }
