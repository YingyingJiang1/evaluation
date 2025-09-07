  private byte[][] clientListParams(final long... clientIds) {
    final byte[][] params = new byte[2 + clientIds.length][];
    int index = 0;
    params[index++] = Keyword.LIST.getRaw();
    params[index++] = ID.getRaw();
    for (final long clientId : clientIds) {
      params[index++] = toByteArray(clientId);
    }
    return params;
  }
